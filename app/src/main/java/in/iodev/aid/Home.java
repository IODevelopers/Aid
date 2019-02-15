package in.iodev.aid;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Home extends AppCompatActivity {
    RecyclerView ngolist;
    JSONArray array;
    Recycleadapter adapter;
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ngolist=findViewById(R.id.ngolist);


        adapter=new Recycleadapter();
        JSONObject object=new JSONObject();
        try {
            object.put("","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HTTPAsyncTask2().execute("https://6ghfrrqsb3.execute-api.ap-south-1.amazonaws.com/Dev/ngo/list",object.toString());

    }

    public void getpercent(View view) {
       dialog dialogBox=new dialog(view.getContext());
        dialogBox.show();
        //Adding width and blur
        Window window=dialogBox.getWindow();
        WindowManager.LayoutParams lp = dialogBox.getWindow().getAttributes();
        lp.dimAmount=0.8f;
        dialogBox.getWindow().setAttributes(lp);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {
        String response="Network Error";

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.

            try {
                response= HTTPPostGet.getJsonResponse(urls[0],urls[1]);
                Log.i("response",response.toString());
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return "Error!";
            }
            finally {

            }

        }
        @Override
        protected void onPreExecute() {

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            JSONObject responseObject;
            try {
                responseObject = new JSONObject(response);
                array=responseObject.getJSONArray("Data");
                Log.i("sample", String.valueOf(array.length()));
                ngolist.setAdapter(adapter);
                ngolist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }


    public class Recycleadapter extends RecyclerView.Adapter<Recycleadapter.SimpleViewHolder> {


        public Recycleadapter() {


        }

        @Override
        public Recycleadapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ngo_card, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final Recycleadapter.SimpleViewHolder holder, final int position) {
            try {

                JSONObject object = null;
                try {
                    object = array.getJSONObject(position);
                    Picasso.get().load(object.getString("ImageURL")).into(holder.im);

                    holder.name.setText(object.getString("Name"));
                    holder.location.setText(object.getString("Location"));
                    holder.cause.setText(object.getString("Cause"));
                    holder.description.setText(object.getString("Description"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
            }


        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return array.length();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            ImageView im;
            TextView name,location,cause,description;
            CheckBox check;


            public SimpleViewHolder(View v) {

                super(v);

                im=findViewById(R.id.avatar);
                name=findViewById(R.id.name);
                location=findViewById(R.id.location);
                cause=findViewById(R.id.cause);
                description=findViewById(R.id.description);
                check=findViewById(R.id.checkbox);
     }
        }}}