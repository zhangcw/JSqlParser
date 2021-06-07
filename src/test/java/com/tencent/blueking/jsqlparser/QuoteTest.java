/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tencent.blueking.jsqlparser;

import com.google.common.collect.ImmutableSet;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class QuoteTest extends JSqlParserBKTestBase {
    public static final Set<String> ASSERTS = ImmutableSet.<String>builder()
            .add("TEST\"1\"")
            .add("TEST''2''")
            .add("TEST'3'")
            .add("TEST\"\"4\"\"")
            .build();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testQuotes() throws Exception {

        final ExpressionVisitorAdapter expressionVisitor = new ExpressionVisitorAdapter() {
            @Override
            public void visit(Column column) {
                super.visit(column);
                Assert.assertTrue(column.getColumnName().contains("column_name"));
            }

            @Override
            public void visit(StringValue value) {
                super.visit(value);
                Assert.assertTrue(value.getValue().equals("string_const"));
                Assert.assertTrue(value.getEscapedValue().contains("string_const"));
            }
        };

        final SelectItemVisitorAdapter selectItemVisitor = new SelectItemVisitorAdapter() {
            @Override
            public void visit(SelectExpressionItem item) {
                super.visit(item);
                item.getExpression().accept(expressionVisitor);
            }
        };

        final SelectVisitorAdapter selectVisitor = new SelectVisitorAdapter() {
            @Override
            public void visit(PlainSelect plainSelect) {
                super.visit(plainSelect);
                for (SelectItem selectItem : plainSelect.getSelectItems()) {
                    selectItem.accept(selectItemVisitor);
                }
            }
        };

        final String sql = "SELECT \"string_const\", 'string_const', `column_name`, [column_name], column_name FROM table";
        testParseAndDeParse(sql);
        Statement parsed = CCJSqlParserUtil.parse(sql);
        parsed.accept(new StatementVisitorAdapter() {
            @Override
            public void visit(Select select) {
                super.visit(select);
                select.getSelectBody().accept(selectVisitor);
            }
        });
    }

    @Test
    public void testEscape() throws JSQLParserException {

        final ExpressionVisitorAdapter expressionVisitor = new ExpressionVisitorAdapter() {

            @Override
            public void visit(StringValue value) {
                super.visit(value);
                Assert.assertTrue(ASSERTS.contains(value.getNotExcapedValue()));
            }
        };

        final SelectItemVisitorAdapter selectItemVisitor = new SelectItemVisitorAdapter() {
            @Override
            public void visit(SelectExpressionItem item) {
                super.visit(item);
                item.getExpression().accept(expressionVisitor);
            }
        };

        final SelectVisitorAdapter selectVisitor = new SelectVisitorAdapter() {
            @Override
            public void visit(PlainSelect plainSelect) {
                super.visit(plainSelect);
                for (SelectItem selectItem : plainSelect.getSelectItems()) {
                    selectItem.accept(selectItemVisitor);
                }
            }
        };

        final String sql = "SELECT \"TEST\"\"1\"\"\", \"TEST''2''\", 'TEST''3''', 'TEST\"\"4\"\"' FROM table";
        testParseAndDeParse(sql);
        Statement parsed = CCJSqlParserUtil.parse(sql);
        parsed.accept(new StatementVisitorAdapter() {
            @Override
            public void visit(Select select) {
                super.visit(select);
                select.getSelectBody().accept(selectVisitor);
            }
        });
    }
}
