package files.reader;

import com.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class CsvFileReader {
    private static Logger logger = LoggerFactory.getLogger(CsvFileReader.class);

    @Getter(AccessLevel.MODULE) @Setter(AccessLevel.MODULE)
    private String fileName;

    public CsvFileReader(){}

    public CsvFileReader(String fileName){
        this.fileName = fileName;
    }

    public List<String[]> readAll() throws FileNotFoundException {
        if(!checkFileType()){
            throw new UnsupportedOperationException("just accept .csv file. current file is "+fileName);
        }
        List<String[]> lines = new ArrayList<>();
        try(CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"))){
            lines = csvReader.readAll();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            logger.error("errors at {} {} {}",CsvFileReader.class,"ReadAll",e);

            throw new FileNotFoundException(e.getMessage());

        }catch (IOException e){
            e.printStackTrace();
            logger.error("errors at {} {} {}",CsvFileReader.class,"ReadAll",e);
        }
        return lines;
    }

    public void readBatch(int batchNum, BiConsumer<String[],List<String[]>> batchReader) throws FileNotFoundException {
        if(!checkFileType()){
            throw new UnsupportedOperationException("just accept .csv file. current file is "+fileName);
        }
        if(batchNum <= 0){
            throw new IllegalArgumentException("Batch number must be greater than 0,current is "+ batchNum);
        }


        List<String[]> lines = new ArrayList<>();
        try(CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"))){
            String[] header;
            header = csvReader.readNext();
            String[] line;
            while((line = csvReader.readNext())!=null){
                lines.add(line);
                if(lines.size() == batchNum){
                    batchReader.accept(header,lines);
                    lines = new ArrayList<>();
                }
            }
            if(lines.size()>0){
                batchReader.accept(header,lines);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
            logger.error("errors at {} {} {}",CsvFileReader.class,"readBatch",e);

            throw new FileNotFoundException(e.getMessage());

        }catch (IOException e){
            e.printStackTrace();
            logger.error("errors at {} {} {}",CsvFileReader.class,"readBatch",e);
        }
    }

    private boolean checkFileType(){
        return fileName.toLowerCase().endsWith(".csv");
    }
}
