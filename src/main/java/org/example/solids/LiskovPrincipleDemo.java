package org.example.solids;


import java.nio.charset.StandardCharsets;

interface FileStorage{
    byte[] readFile(String filePath);
    String uploadFile(byte[] fileData);
    void deleteFile(String filePath);
}


interface ReadOnlyFileStorage{
    byte[] readFile(String filePath);

}

class ReadOnlyFileStorageService implements ReadOnlyFileStorage{

    @Override
    public byte[] readFile(String filePath) {
        System.out.println("successfully read the file");
        return new byte[0];
    }

}


class FileStorageService implements FileStorage{

    @Override
    public byte[] readFile(String filePath) {
        return new byte[0];
    }

    @Override
    public String uploadFile(byte[] fileData) {
        System.out.println("Upload successfully");
        return "data/files/001";
    }

    @Override
    public void deleteFile(String filePath) {
        System.out.println("Successfully deleted");
    }
}

class AadhaarVerifyService{

    private FileStorage fileStorage;

    public AadhaarVerifyService(FileStorage fileStorage){
        this.fileStorage = fileStorage;
    }


    public void validateAadhaar(String file){
        // upload to fileStorage
        String filePath = this.fileStorage.uploadFile(file.getBytes(StandardCharsets.UTF_8));
        // db save the filePath and other details
        // verification
        this.fileStorage.deleteFile(filePath);
    }

}


public class LiskovPrincipleDemo {

    public static void main(String[] args) {
        FileStorageService fileStorageService = new FileStorageService();
        ReadOnlyFileStorageService readOnlyFileStorageService = new ReadOnlyFileStorageService();
        AadhaarVerifyService aadhaarVerifyService = new AadhaarVerifyService(fileStorageService);
        String aadhaar = "some random data";
        aadhaarVerifyService.validateAadhaar(aadhaar);
    }
}
