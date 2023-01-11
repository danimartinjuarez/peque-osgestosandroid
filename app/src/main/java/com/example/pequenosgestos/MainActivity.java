package com.example.pequenosgestos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText editTextUserId, editTextName, editTextPhoto, editTextWhatOffer, editTextWhatLike, editTextLocation, editTextContact, editTextTitle;
    Spinner spinnerActivityType;
    ProgressBar progressBar;
    ListView listView;
    Button buttonAddUpdate;

    List<User> usersList;
    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserId = findViewById(R.id.editTextUserId);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoto = findViewById(R.id.editTextPhoto);
        editTextWhatOffer = findViewById(R.id.editTextWhatOffer);
        editTextWhatLike = findViewById(R.id.editTextWhatLike);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextContact = findViewById(R.id.editTextContact);
        editTextTitle = findViewById(R.id.editTextTitle);
        spinnerActivityType = findViewById(R.id.spinnerActivityType);
        buttonAddUpdate = findViewById(R.id.buttonAddUpdate);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listViewUsers);

       usersList = new ArrayList<>();

        buttonAddUpdate.setOnClickListener(view -> {
            //if it is updating
            if (isUpdating) {

                //updateUser();
            } else {

                createUser();
            }
        });
    }

    private void createUser() {
        String name = editTextName.getText().toString().trim();
        String photo = editTextPhoto.getText().toString().trim();
        String whatOffer = editTextWhatOffer.getText().toString().trim();
        String whatLike = editTextWhatLike.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String title = editTextTitle.getText().toString().trim();
        String activity = spinnerActivityType.getSelectedItem().toString();


        //validating the inputs
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Te falta el nombre");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(photo)) {
            editTextPhoto.setError("No has puesto foto");
            editTextPhoto.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(whatOffer)) {
            editTextPhoto.setError("No has puesto que ofreces");
            editTextPhoto.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(whatLike)) {
            editTextPhoto.setError("No has puesto que ofreces");
            editTextPhoto.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(location)) {
            editTextPhoto.setError("No has puesto que ofreces");
            editTextPhoto.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            editTextPhoto.setError("No has puesto que ofreces");
            editTextPhoto.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(title)) {
            editTextPhoto.setError("No has puesto que ofreces");
            editTextPhoto.requestFocus();
            return;
        }
        //if validation passes

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("photo", photo);
        params.put("what_offer", whatOffer);
        params.put("what_like", whatLike);
        params.put("location", location);
        params.put("contact", contact);
        params.put("title", title);

        params.put("activity_type", activity);


        //Calling the create user API
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_USER, params, CODE_POST_REQUEST);
        request.execute();
    }


    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject (s);
                System.out.println (jsonObject);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshUserList(jsonObject.getJSONArray("users"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class UserAdapter extends ArrayAdapter<User> {

        //our users list
        List<User> userList;


        //constructor to get the list
        public UserAdapter(List<User> userList) {
            super(MainActivity.this, R.layout.layout_user_list, userList);
            this.userList = userList;
        }


        //method returning list item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem;
            listViewItem = inflater.inflate (R.layout.layout_user_list, null, true);

            //getting the textview for displaying name
            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            //the update and delete textview
            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final User user = usersList.get(position);

            textViewName.setText(user.getName());

            //attaching click listener to update
            textViewUpdate.setOnClickListener(view -> {
                //so when it is updating we will
                //make the isUpdating as true
                isUpdating = true;

                //we will set the selected user to the UI elements
                editTextUserId.setText(String.valueOf(user.getId()));
                editTextName.setText(user.getName());
                editTextPhoto.setText(user.getPhoto());
                editTextWhatOffer.setText(user.getWhatOffer());
                editTextWhatLike.setText(user.getWhatLike());
                editTextLocation.setText(user.getLocation());
                editTextContact.setText(user.getContact());
                editTextTitle.setText(user.getTitle());

                spinnerActivityType.setSelection(((ArrayAdapter<String>) spinnerActivityType.getAdapter()).getPosition(user.getActivityType()));

                //we will also make the button text to Update
                buttonAddUpdate.setText("Update");
            });

            //when the user selected delete
            textViewDelete.setOnClickListener(view -> {

                // we will display a confirmation dialog before deleting
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Delete " + user.getName())
                        .setMessage("Are you sure you want to delete it?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            //if the choice is yes we will delete the user
                            //method is commented because it is not yet created
                            //deleteUser(user.getId());
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(R.drawable.ic_launcher_foreground)
                        .show();

            });

            return listViewItem;
        }
    }
    private void readUsers() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_USERS, null, CODE_GET_REQUEST);
        request.execute();
    }
    private void refreshUserList(JSONArray users) throws JSONException {
        //clearing previous users
        usersList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < users.length(); i++) {
            //getting each user object
            JSONObject obj = users.getJSONObject(i);

            //adding the user to the list
            usersList.add(new User(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("photo"),
                    obj.getString("whatOffer"),
                    obj.getString("whatLike"),
                    obj.getString("location"),
                    obj.getString("contact"),
                    obj.getString("title"),
                    obj.getString("activityType")

            ));
        }

        //creating the adapter and setting it to the listview
        UserAdapter adapter = new UserAdapter(usersList);
        listView.setAdapter(adapter);
    }
}