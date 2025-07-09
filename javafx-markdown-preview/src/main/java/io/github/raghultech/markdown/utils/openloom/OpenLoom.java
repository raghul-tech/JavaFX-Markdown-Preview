/*
 * Copyright (C) 2025 Raghul-tech
 * https://github.com/raghul-tech
 * This file is part of JavaFX Markdown Preview.
 *
 * JavaFX Markdown Preview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JavaFX Markdown Preview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JavaFX Markdown Preview.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.raghultech.markdown.utils.openloom;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewFileException;

public class OpenLoom {
    

      
    
        public static  StringBuilder getContent(File openFile) {
            if (openFile == null || !openFile.exists()) {
            	  throw new MarkdownPreviewFileException("File not found or invalid: " + openFile);
            }
        double fileSizeInMB = openFile.length() / (1024.0 * 1024.0); // Convert bytes to MB
      
                    if (fileSizeInMB < 20.0) {  
                     return   loadSmallFile(openFile);
                    }  else if (fileSizeInMB < 60.0) {  
                  return  	 loadLargeFile(openFile); 	  
                    }else if (fileSizeInMB <90.0 ){
                    return	loadVeryLargeFile(openFile);
                    	
                    }else {
                    return	loadBigFile(openFile);
                    }  
        }
        
   
        
        private static StringBuilder loadSmallFile(File file) throws OutOfMemoryError {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

                // Use an initial capacity if you expect the file to be large-ish
                StringBuilder content = new StringBuilder((int) file.length());

                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                return content;

            } catch (IOException e) {
            	 throw new MarkdownPreviewFileException("Failed to read content from file: " + file, e);
            }
        }

    
        private static StringBuilder loadLargeFile(File file) throws OutOfMemoryError {
            Charset charset = StandardCharsets.UTF_8;
            CharsetDecoder decoder = charset.newDecoder();
            StringBuilder content = new StringBuilder((int) Math.min(file.length(), Integer.MAX_VALUE)); // Safe initial capacity

            try (RandomAccessFile raf = new RandomAccessFile(file, "r");
                 FileChannel fileChannel = raf.getChannel()) {

                ByteBuffer buffer = ByteBuffer.allocate(65536); // 64KB buffer
                while (fileChannel.read(buffer) > 0) {
                    buffer.flip();
                    CharBuffer charBuffer = decoder.decode(buffer);
                    content.append(charBuffer);
                    buffer.clear();
                }

            } catch (IOException e) {
            	 throw new MarkdownPreviewFileException("Failed to read content from file: " + file, e);
            }

            return content;
        }


        private static StringBuilder loadVeryLargeFile(File file) throws OutOfMemoryError {
            final int BUFFER_SIZE = 32 * 1024; // 32KB
            StringBuilder content = new StringBuilder((int) Math.min(file.length(), Integer.MAX_VALUE));

            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr, BUFFER_SIZE)) {

                char[] buffer = new char[BUFFER_SIZE];
                int charsRead;

                while ((charsRead = reader.read(buffer)) != -1) {
                    content.append(buffer, 0, charsRead);
                }

            } catch (IOException e) {
            	 throw new MarkdownPreviewFileException("Failed to read content from file: " + file, e);
            }

            return content;
        }


        private static StringBuilder loadBigFile(File file) throws OutOfMemoryError {
            try (RandomAccessFile raf = new RandomAccessFile(file, "r");
                 FileChannel fileChannel = raf.getChannel()) {

                long fileSize = fileChannel.size();
                long maxLoadSize = Math.min(fileSize, Integer.MAX_VALUE); // Avoid overflow

                MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, maxLoadSize);
                byte[] byteArray = new byte[(int) maxLoadSize];
                buffer.get(byteArray);

                String content = new String(byteArray, StandardCharsets.UTF_8);

                return new StringBuilder(content);

            } catch (IOException e) {
            	 throw new MarkdownPreviewFileException("Failed to read content from file: " + file, e);
            }
        }

   
 


}





