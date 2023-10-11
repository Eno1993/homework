package com.example.homework;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class FileService {

    // todo FILE 과 MultipartFile 의 차이점
    /*File 의 경우 java 에서 제공하는 기능이지만 MultipartFile 의 경우 spring 에서 제공하는 기능이다.
    보편적으로 File 보다 MultipartFile 인터페이스가 파일의 정보를 핸들러에서 손쉽게 다룰 수 있게 도와주기 때문에 사용한다.*/

    //todo 실제 라이브러리를 사용하기 위해서는 라이브러리를 build 해줘야한다.
    //  springBoot 사용시 자동으로 등록 되지만 그게 아닐 경우 라이브러리를 build 해줘야 한다.
    //  compile group: 'commons-fileupload', name: 'commons-fileupload', version: '1.4'
    //	compile group: 'commons-io', name: 'commons-io', version: '2.4'

    public File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

    public MultipartFile fileToMultipartFile(File file) throws IOException{

        String fieldName = "file";

        FileItem fileItem =
                new DiskFileItem(
                        fieldName,
                        Files.probeContentType(file.toPath()),
                        false,
                        file.getName(),
                        Long.valueOf(file.length()).intValue(),
                        file.getParentFile());

        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = fileItem.getOutputStream();

        IOUtils.copy(inputStream, outputStream);
        return new CommonsMultipartFile(fileItem);
    }

    // todo 특정 파일 혹은 디렉토리가 존재하는지 확인
    /*두 가지 library 를 통해서 확인 할 수 있음
    1. java.io.File
    2. java.nio.file.Files*/
    public void checkFileOrDirectory(){
        String filePath = "filePath";
        //-----------------------------------------------
        File file = new File(filePath);
        if(file.exists()){
            if(file.isDirectory()){// 디렉토리 존재
            }else{// 디렉토리가 없음
                if(file.exists()){// 파일 존재
                }
            }
        }else{// 파일과 디렉토리 모두 존재 하지 않음
        }
        //-----------------------------------------------
        Path path = Paths.get(filePath);
        if(Files.exists(path)){
            if(Files.isDirectory(path)){// 디렉토리 존재
            }else{
                if(Files.isRegularFile(path)){// 파일 존재
                }
            }
        }else{// 파일과 디렉토리 모두 존재 하지 않음
        }
    }

    // todo file creation
    /*1. java.io.File
    2. java.io.FileOutputStream
    3. java.nio.file.Files*/
    public void createFile(){
        String filePath = "filePath";
        File file = new File(filePath);
        //-------------------------------------------------
        try {
            if (file.createNewFile()) {// 파일 생성
            } else {// 파일 이미 존재 -> 아무작업도 실행 하지 않음
            }
        } catch (IOException e) {// 지정한 경로가 존재하지 않음
            e.printStackTrace();
        }
        //-------------------------------------------------
        try{
            FileOutputStream fileOutputStreamAppendTrue = new FileOutputStream(file, true);// 기존의 파일이 존재할 경우 이어서 쓰기
            FileOutputStream fileOutputStreamAppendFalse = new FileOutputStream(file, false);// 기존의 파일을 무시하고 새로쓰기
        }catch (FileNotFoundException e){// 지정한 경로가 존재하지 않음
            e.printStackTrace();
        }
        //-------------------------------------------------
        Path path = Paths.get(filePath);

        try {
            Path newFilePath = Files.createFile(path);// 기존의 파일이 존재하지 않을 경우 저장하고 경로 반환
        } catch (NoSuchFileException e) {// 지정한 경로가 존재하지 않음
            e.printStackTrace();
        } catch (FileAlreadyExistsException e){// 기존의 파일이 존재할 경우
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // todo 파일 읽기
    /*1. FileReader
    2. BufferedReader
    3. Scanner
    4. Files*/
    public void readFile(){
        String filePath = "filePath";
        // FileReader 의 경우 character 파일을 읽을 수 있고 인코딩관련 설정은 java11 가능
        // InputStreamReader 를 상속받고 read() 메서드를 이용해서 한글자의 char 씩 읽을 수있음
        try{
            FileReader fileReader = new FileReader(filePath);
            int ch;
            while((ch = fileReader.read()) != -1){
                System.out.print((char) ch);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //------------------------------------------------------------------------
        //BufferedReader 는 buffer 를 사용하기 때문에 FileReader 보다 좀 더 효율적으로 값을 읽을 수 있다.
        //개행 문자를 기준으로 한 줄 씩 String 을 읽어들임
        //BufferedReader 는 Reader 객체와 버퍼 사이즈를 전달하여 생성하며 기본 버퍼 사이즈는 8KB
        try{
            int bufferSize = 10000000;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath), bufferSize);

            String str;
            while((str = bufferedReader.readLine()) != null){
                System.out.println(str);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        // 추가적으로 인코딩을 지정해서 읽을 수도 있음
        //BufferedReader encodingBufferedReader = new BufferedReader(new FileReader(filePath), Charset.forName("UTF-8"));
        // 혹은 FileReader 가 아닌 InputStreamReader 를 이용해서 읽을 수 있음.
        //BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)), "UTF-8");
        //------------------------------------------------------------------------
        //Scanner 를 이용하면 delimiter를 이용하여 분리해서 읽을 수 있음
        //delimiter 는 문법의 끝을 나타내므로 일반적으로 '\t', '\f', '\r', ' ', '\n' 를 의미한다.
        try{
            Scanner scanner = new Scanner(new File("d:\\file.txt"));
            while (scanner.hasNext()) {
                String str = scanner.next();
                System.out.println(str);
            }
            //Scanner.nextLine() 메서드를 이용하면 개행을 기준으로 파일을 읽어 들일 수 있음
            while(scanner.hasNextLine()){
                String str = scanner.nextLine();
                System.out.println(str);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        //-----------------------------------------------------------------------
        /*List나 배열 혹은 String 에 쉽게 담을 수 있도록 도와주는 Files 클래스를 이용하는 방법도 있다
        java7에서부터 사용할 수 있으며 모든 메소드가 static 으로 선언되어있는게 특징 */
        try{
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path); // 개행을 기준으로 분리
            byte[] bytes = Files.readAllBytes(path); // byte 배열로 변환
            String str = Files.readString(path); // 단일 String 으로 변환
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
