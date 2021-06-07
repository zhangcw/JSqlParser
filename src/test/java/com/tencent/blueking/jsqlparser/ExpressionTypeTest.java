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

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperatorType;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

public class ExpressionTypeTest extends JSqlParserBKTestBase {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testExpressionType() throws Exception {
        Reflections reflections = new Reflections();
        Set<Class<? extends Expression>> classes = reflections.getSubTypesOf(Expression.class);
        for (Class<? extends Expression> clazz : classes) {
            if (!Modifier.isInterface(clazz.getModifiers())
                    && !Modifier.isAbstract(clazz.getModifiers())) {
                Constructor<?>[] constructors = clazz.getConstructors();
                Constructor<?> constructor = constructors[0];
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] params = new Object[parameterTypes.length];
                if (clazz.getCanonicalName().equals(TimestampValue.class.getCanonicalName())) {
                    params[0] = "/2000-01-01 00:00:00/";
                } else if (clazz.getCanonicalName().equals(LongValue.class.getCanonicalName())) {
                    params[0] = parameterTypes[0].getCanonicalName().equals(long.class.getCanonicalName()) ? 0L : "0L";
                } else if (clazz.getCanonicalName().equals(SignedExpression.class.getCanonicalName())) {
                    params[0] = '+';
                } else if (clazz.getCanonicalName().equals(DateValue.class.getCanonicalName())) {
                    params[0] = "/2000-01-01/";
                } else if (clazz.getCanonicalName().equals(TimeValue.class.getCanonicalName())) {
                    params[0] = "/00:00:00/";
                } else if (clazz.getCanonicalName().equals(RegExpMySQLOperator.class.getCanonicalName())) {
                    params[0] = RegExpMatchOperatorType.MATCH_CASEINSENSITIVE;
                } else if (clazz.getCanonicalName().equals(DoubleValue.class.getCanonicalName())) {
                    params[0] = "0.0";
                } else if (clazz.getCanonicalName().equals(RegExpMatchOperator.class.getCanonicalName())) {
                    params[0] = RegExpMatchOperatorType.MATCH_CASEINSENSITIVE;
                } else if (clazz.getCanonicalName().equals(JdbcParameter.class.getCanonicalName())) {
                    if (parameterTypes.length == 2) {
                        params[1] = false;
                    }
                } else if (clazz.getCanonicalName().equals(StringValue.class.getCanonicalName())) {
                    params[0] = "ARG";
                }
                try {
                    Expression expression = (Expression) constructor.newInstance(params);
                    Assert.assertThat(
                            removeUnderlines(expression.getExpressionType()),
                            new IsEqualIgnoringCase(expression.getClass().getSimpleName())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }

            }
        }
    }

    private static String removeUnderlines(String original) {
        return original.replaceAll("_", "");
    }
}
