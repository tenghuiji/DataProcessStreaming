package files.reader;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CsvFileReaderTest {
    private CsvFileReader csvFileReader;
    @Before
    public void setUp() throws Exception {
        csvFileReader = new CsvFileReader();
    }

    @Test
    public void readAll() throws FileNotFoundException {
        String fileName = getFile("SaleRecords.csv");
        csvFileReader.setFileName(fileName);
        List<String[]> lines = csvFileReader.readAll().stream()
                .filter(item->!item[0].isEmpty())
                .collect(Collectors.toList());
        assertThat(13,equalTo(lines.size()));
    }

    @Test
    public void readAll_new_object() throws FileNotFoundException {
        String fileName = getFile("SaleRecords.csv");
        List<String[]> lines = new CsvFileReader(fileName).readAll().stream()
                .filter(item->!item[0].isEmpty())
                .collect(Collectors.toList());
        assertThat(13,equalTo(lines.size()));
    }

    @Test(expected = NullPointerException.class)
    public void readAll_Exception_null_point() throws FileNotFoundException {
        //String fileName = getFile("SaleRecords.csv");
        csvFileReader.setFileName(null);
        List<String[]> lines = csvFileReader.readAll().stream()
                .filter(item->!item[0].isEmpty())
                .collect(Collectors.toList());
        assertThat(13,equalTo(lines.size()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void readAll_Exception_wrong_file_type() throws FileNotFoundException {
        String fileName = getFile("SaleRecords.xlsx");
        csvFileReader.setFileName(fileName);
        List<String[]> lines = csvFileReader.readAll().stream()
                .filter(item->!item[0].isEmpty())
                .collect(Collectors.toList());
        assertThat(13,equalTo(lines.size()));
    }

    @Test(expected = FileNotFoundException.class)
    public void readAll_Exception_FileNotFound() throws FileNotFoundException {
        //String fileName = getFile("SaleRecords.csv");
        csvFileReader.setFileName("SaleRecords.csv");
        List<String[]> lines = csvFileReader.readAll().stream()
                .filter(item->!item[0].isEmpty())
                .collect(Collectors.toList());
        assertThat(13,equalTo(lines.size()));
    }

    @Test
    public void checkFileType() throws Exception {
        csvFileReader.setFileName("abc.csv");
        assertThat(true,is((boolean)Whitebox.invokeMethod(csvFileReader,"checkFileType")));
    }

    @Test
    public void checkFileType_UpperCast() throws Exception {
        csvFileReader.setFileName("abc.CSV");
        assertThat(true,is((boolean)Whitebox.invokeMethod(csvFileReader,"checkFileType")));
    }

    @Test
    public void checkFileType_Exception_wrong_file_tyep_xls() throws Exception {
        csvFileReader.setFileName("abc.xls");
        assertThat(false,is((boolean)Whitebox.invokeMethod(csvFileReader,"checkFileType")));
    }

    @Test
    public void readerBatch() throws FileNotFoundException {
        String fileName = getFile("SaleRecords.csv");
        csvFileReader.setFileName(fileName);
        csvFileReader.readBatch(100,(header,line)->{
            assertThat(14,is(header.length));
            List<String[]> lines = line.stream()
                    .filter(item->!item[0].isEmpty())
                    .collect(Collectors.toList());
            assertThat(12,is(lines.size()));
        });
    }

    @Test
    public void readerBatch_12_recodes() throws FileNotFoundException {
        String fileName = getFile("SaleRecords.csv");
        csvFileReader.setFileName(fileName);
        csvFileReader.readBatch(12,(header,line)->{
            assertThat(14,is(header.length));
            List<String[]> lines = line.stream()
                    .filter(item->!item[0].isEmpty())
                    .collect(Collectors.toList());
        });
    }

    @Test(expected = NullPointerException.class)
    public void readerBatch_Exception_No_file() throws FileNotFoundException {
        csvFileReader.readBatch(100,(header,line)->{
            assertThat(14,is(header));
            List<String[]> lines = line.stream()
                    .filter(item->!item[0].isEmpty())
                    .collect(Collectors.toList());
            assertThat(5,is(lines.size()));
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void readerBatch_Exception_Negative() throws FileNotFoundException {
        String fileName = getFile("SaleRecords.csv");
        csvFileReader.setFileName(fileName);
        csvFileReader.readBatch(-100,(header,line)->{
            assertThat(14,is(header));
            List<String[]> lines = line.stream()
                    .filter(item->!item[0].isEmpty())
                    .collect(Collectors.toList());
            assertThat(5,is(lines.size()));
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void readerBatch_Exception_Woring_file_tyep() throws FileNotFoundException {
        String fileName = getFile("SaleRecords.xlsx");
        csvFileReader.setFileName(fileName);
        csvFileReader.readBatch(-100,(header,line)->{
            assertThat(14,is(header));
            List<String[]> lines = line.stream()
                    .filter(item->!item[0].isEmpty())
                    .collect(Collectors.toList());
            assertThat(5,is(lines.size()));
        });
    }

    @Test(expected = FileNotFoundException.class)
    public void readerBatch_Exception_FileNotFound() throws FileNotFoundException {
        //String fileName = getFile("SaleRecords.xlsx");
        csvFileReader.setFileName("SaleRecords.csv");
        csvFileReader.readBatch(100,(header,line)->{
            assertThat(14,is(header));
            List<String[]> lines = line.stream()
                    .filter(item->!item[0].isEmpty())
                    .collect(Collectors.toList());
            assertThat(5,is(lines.size()));
        });
    }

    private String getFile(String fileName){
        return CsvFileReaderTest.class.getResource("/"+fileName).getPath();
    }
}