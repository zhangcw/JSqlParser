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

import net.sf.jsqlparser.JSQLParserException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Note: Created by hongzezhang on 2017/7/10.
 */
public class BooleanParameterTest extends JSqlParserBKTestBase {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBooleanParameter1() throws JSQLParserException {
        testParseAndDeParse("SELECT " +
                "avg(CASE PlatID WHEN 0 THEN 10 WHEN 1 THEN 20 ELSE 0 END, " +
                "iZoneAreaID >= 14000) AS platid_avg " +
                "FROM etl_PlayerRegister " +
                "WHERE iZoneAreaID >= 10000 " +
                "GROUP BY cc_set, version, cc_set_trans");

    }

    @Test
    public void testBooleanParameter2() throws JSQLParserException {
        testParseAndDeParse("SELECT avg(iZoneAreaID >= 14000) AS platid_avg " +
                "FROM etl_PlayerRegister " +
                "WHERE isOK(f) " +
                "GROUP BY cc_set, version, cc_set_trans");

    }

    @Test
    public void testBooleanParameter3() throws JSQLParserException {
        testParseAndDeParse("SELECT sum(CASE WHEN is_done = 1 THEN 1 ELSE 0 END) AS downloadSuc FROM dltool_parse");

    }

    @Test
    public void testBooleanParameter4() throws JSQLParserException {
        testParseAndDeParse("SELECT avg(iZoneAreaID >= 14000) AS platid_avg FROM etl_PlayerRegister WHERE isOK(f) GROUP BY cc_set, version, cc_set_trans");

    }

    @Test
    public void testBooleanParameter5() throws JSQLParserException {
        testParseAndDeParse("SELECT sum(if(exitReason = '0' OR exitReason = '1', 1, 0)) AS suc_rate FROM TCLS_Global_uin");

    }

    @Test
    public void testBooleanParameter6() throws JSQLParserException {
        testParseAndDeParse("SELECT sum(ccid) AS cnt FROM test001");

    }


    @Test
    public void testBooleanParameter7() throws JSQLParserException {
        testParseAndDeParse("SELECT count(timecost <= 400 AND timecost > 0) FROM aaa");


    }
}
