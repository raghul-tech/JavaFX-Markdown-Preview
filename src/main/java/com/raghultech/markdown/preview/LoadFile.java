package com.raghultech.markdown.preview;


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

import com.raghultech.markdown.exception.MarkdownPreviewFileException;

public class LoadFile {
    

      
    
        protected static  StringBuilder getContent(File openFile) {
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





