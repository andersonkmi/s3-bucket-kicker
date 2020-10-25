package org.codecraftlabs.kikker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayName("File util unit test")
public class FileUtilTest {
    @Test
    void simpleTest() {
        Map<String, String> results = FileUtil.listFiles(".", ".java");
        System.out.println(results);
    }
}
