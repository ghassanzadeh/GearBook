package com.example.ghassanz_gearbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
    This class is the main activity in the entire application and connects all features in the app.
    It contains instance of various classes in android within the context of this application such as
    FloatingActionButton, TextView, EditText, Buttons, and etc. This is a boundary class and connects the information
    pass from the dialogs for add and update/delete/view to the listview of gearlist.
    Reference: https://www.youtube.com/watch?v=fis26HvvDII&t=32362s created by Meisam Mansourzadeh on May 26, 2020.
               I started learning the materials since Sept 15, 2020 and used the code for listview and FloatingActionButton, MaterialDesgin for this assignment.
 */

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab; // add a new gear to the list
    ArrayList<Gear> gears = new ArrayList<>();
    private ListView gearsListView;
    GearListAdapter adapter;
    private TextView totalPrice; // shows the total amount in the shopping list
    private double total_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gearsListView = (ListView) findViewById(R.id.gearsListView);
        fab = findViewById(R.id.fab);

        totalPrice = findViewById(R.id.total_price); // update the total amount

        adapter = new GearListAdapter(this, R.layout.adapter_view_layout, gears);
        gearsListView.setAdapter(adapter);

        // clicking on the floating button on the main page of the app opens a dialog for the user
        // to add a new gear to the bottom of the list
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog();
            }
        });

        // clicking on each item in the listview opens a dialog that the user can see the information about
        // a particular gear, update the information or delete the gear from the list
        gearsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // create a dialog to update/delete/view a gear from the list
                AlertDialog.Builder updateBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View updateView = inflater.inflate(R.layout.update_data, null);
                final AlertDialog updDialog = updateBuilder.create();
                updDialog.setView(updateView);

                // declare the EditText related to the information of each gear in the gear list
                final EditText upd_Date = updateView.findViewById(R.id.upd_date);
                final EditText upd_Maker = updateView.findViewById(R.id.upd_maker);
                final EditText upd_Description = updateView.findViewById(R.id.upd_description);
                final EditText upd_price = updateView.findViewById(R.id.upd_amount);
                final EditText upd_comment = updateView.findViewById(R.id.upd_comment);

                // instantiating the buttons on the update/delete/view dialog
                Button upd_btnUpdate = updateView.findViewById(R.id.upd_btn_update);
                Button upd_btnDelete = updateView.findViewById(R.id.upd_btn_delete);
                Button upd_btnCancel = updateView.findViewById(R.id.upd_btn_cancel);

                // set data from listview to the update dialog
                upd_Date.setText(gears.get(position).getDate());
                upd_Maker.setText(gears.get(position).getMaker());
                upd_Description.setText(gears.get(position).getDescription());
                upd_price.setText(gears.get(position).getPrice());
                upd_comment.setText(gears.get(position).getComment());

                // clicking on the update button allows the user to update the information of a selected gear from the list
                upd_btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // gets information from each EditText
                        String uDate = upd_Date.getText().toString().trim();
                        String uMaker = upd_Maker.getText().toString().trim();
                        String uDescription = upd_Description.getText().toString().trim();
                        String uPrice = upd_price.getText().toString().trim();
                        String uComment = upd_comment.getText().toString().trim();

                        // regex to validate if the user entered the date in a correct format given by the hint
                        Pattern pattern_udate = Pattern.compile("^([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$");
                        Matcher match_udate = pattern_udate.matcher(uDate);
                        if (match_udate.matches() == false){
                            upd_Date.setError("Date format invalid");
                            return;
                        }

                        // regex to validate if the user entered the price in a correct format given by the hint
                        Pattern pattern_uprice = Pattern.compile("^([0-9]+)\\.([0-9]{2})$");
                        Matcher match_uprice = pattern_uprice.matcher(uPrice);
                        if (match_uprice.matches() == false){
                            upd_price.setError("Price format invalid");
                            return;
                        }

                        // checks if the fields are empty to warn the user to fill out the edittexts
                        if (TextUtils.isEmpty(uDate)){
                            upd_Date.setError("Date Required");
                            return;
                        }

                        if (TextUtils.isEmpty(uMaker)){
                            upd_Maker.setError("Maker Required");
                            return;
                        }

                        if (TextUtils.isEmpty(uDescription)){
                            upd_Description.setError("Description Required");
                            return;
                        }
                        if (TextUtils.isEmpty(uPrice)){
                            upd_price.setError("Price Required");
                            return;
                        }

                        // creates a new instance of the class Gear
                        Gear item = new Gear( uDate, uMaker, uDescription, uPrice, uComment);
                        gears.set(position, item);
                        getTotal(gears);

                        // updates the total amount in the shopping list
                        totalPrice.setText("Total CAD: " + String.valueOf(total_amount));
                        // notifies the adapter of any data changes
                        adapter.notifyDataSetChanged();
                        // shows a toast message to the user that a gear item is updated
                        Toast.makeText(MainActivity.this, "Gear Item Updated", Toast.LENGTH_SHORT).show();
                        updDialog.dismiss();
                    }
                });

                // clicking on the delete button to delete a selected gear from the gear list
                upd_btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gears.remove(position);
                        getTotal(gears);
                        totalPrice.setText("Total CAD: "+ String.valueOf(total_amount));
                        adapter.notifyDataSetChanged();
                        updDialog.dismiss();
                    }
                });

                // clicking on the cancel button to dismiss the update/delete/view dialog
                upd_btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updDialog.dismiss();
                    }
                });
                updDialog.show();
            }
        });
    }

    // create add dialog to add a new gear to the gear list
    private void addDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View myView = LayoutInflater.from(MainActivity.this).inflate(R.layout.input_data, null, false);
        final AlertDialog dialog = builder.create();
        dialog.setView(myView);

        // instantiate the EditText related to the information of each gear in the gear list
        final EditText edtDate = myView.findViewById(R.id.edt_date);
        final EditText edtMaker = myView.findViewById(R.id.edt_maker);
        final EditText edtDescription = myView.findViewById(R.id.edt_description);
        final EditText edtPrice = myView.findViewById(R.id.edt_amount);
        final EditText edtComment = myView.findViewById(R.id.edt_comment);

        // instantiating the buttons on the add dialog
        Button btnSave = myView.findViewById(R.id.btn_save);
        Button btnCancel = myView.findViewById(R.id.btn_cancel);

        // clicking on the save button add a new gear to the gear list given all required fields are filled out by the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gets information from each EditText
                String mDate = edtDate.getText().toString().trim();
                String mMaker = edtMaker.getText().toString().trim();
                String mDescription = edtDescription.getText().toString().trim();
                String mPrice = edtPrice.getText().toString().trim();
                String mComment = edtComment.getText().toString().trim();

                // regex to validate if the user entered the date in a correct format given by the hint
                Pattern pattern_date = Pattern.compile("^([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$");
                Matcher match_date = pattern_date.matcher(mDate);
                if (match_date.matches() == false){
                    edtDate.setError("Date format invalid");
                    return;
                }

                // regex to validate if the user entered the price in a correct format given by the hint
                Pattern pattern_price = Pattern.compile("^([0-9]+)\\.([0-9]{2})$");
                Matcher match_price = pattern_price.matcher(mPrice);
                if (match_price.matches() == false){
                    edtPrice.setError("Price format invalid");
                    return;
                }

                // checks if the fields are empty to warn the user to fill out the edittexts
                if (TextUtils.isEmpty(mDate)){
                    edtDate.setError("Date Required");
                    return;
                }
                if (TextUtils.isEmpty(mMaker)){
                    edtMaker.setError("Maker Required");
                    return;
                }
                if (TextUtils.isEmpty(mDescription)){
                    edtDescription.setError("Decription Required");
                    return;
                }
                if (TextUtils.isEmpty(mPrice)){
                    return;
                }

                // creates a new instance of the class Gear
                Gear item = new Gear( mDate, mMaker, mDescription, mPrice, mComment);
                gears.add(item);
                getTotal(gears);

                // updates the total amount in the shopping list
                totalPrice.setText("Total CAD: " + String.valueOf(total_amount));
                // notifies the adapter of any data changes
                adapter.notifyDataSetChanged();
                // shows a toast message to the user that a gear item is added
                Toast.makeText(MainActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // clicking on the cancel button to dismiss the add dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    // calculates the total amount of gears prices in the gear list
    public double getTotal(ArrayList<Gear> list){
        total_amount = 0;
        for (int i = 0; i< list.size(); i++){
            total_amount = total_amount + Double.parseDouble(list.get(i).getPrice());
        }
        return total_amount;
    }
}