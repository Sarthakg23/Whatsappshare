package com.example.sharewhatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public static final int PICKFILE_RESULT_CODE = 1;

    private Uri fileUri;
    private String filePath;
    private  EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b=(Button)findViewById(R.id.button);
        ed=(EditText)findViewById(R.id.editText);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ed.getText().toString().equals("")) {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                }
                else
                    Toast.makeText(getApplicationContext(),"Enter Message",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    //Toast.makeText(getApplicationContext(),fileUri.toString(),Toast.LENGTH_SHORT).show();
                    try {
                        String root = Environment.getExternalStorageDirectory().toString();
                        InputStream inputStream = getContentResolver().openInputStream(fileUri);
                        XSSFWorkbook myWorkBook = new XSSFWorkbook(inputStream);
// Get the first sheet from workbook
                        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

                        Iterator<Row> rowIter = mySheet.rowIterator();
                        while (rowIter.hasNext()) {
                            XSSFRow myRow = (XSSFRow) rowIter.next();
                            Iterator<Cell> cellIter = myRow.cellIterator();
                            String sno = "";
                            while (cellIter.hasNext()) {
                                XSSFCell myCell = (XSSFCell) cellIter.next();
                                sno = myCell.toString();
                                //Toast.makeText(getApplicationContext(), sno, Toast.LENGTH_SHORT).show();
                                String text = "This is a test";
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + sno + "&text=" + ed.getText().toString()));
                                startActivity(intent);
                            }
                        }
                    }
                    catch (IOException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }



                break;
        }
    }
}
