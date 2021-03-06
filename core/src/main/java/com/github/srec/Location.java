/*
 * Copyright 2010 Victor Tatai
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */
package com.github.srec;

/**
 * Represents a location within a test file.
 * 
 * @author Victor Tatai
 */
public class Location {
    private String fileName;
    private int lineNumber;
    private int column;
    private String line;

    public Location(String fileName, int lineNumber, int column, String line) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.column = column;
        this.line = line;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumn() {
        return column;
    }

    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "file '" + fileName + '\'' +
                ", line " + lineNumber +
                ", column " + column +
                ", text '" + line + '\'';
    }
}
