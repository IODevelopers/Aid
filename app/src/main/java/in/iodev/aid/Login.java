package in.iodev.aid;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
    }

    public void signin(View view) {
        JSONObject object=new JSONObject();
        try {
            object.put("username",username.getText().toString());
            object.put("password",password.getText().toString());
            Log.i("response",object.toString());
        }
        catch (Exception e)
        {

        }
        new HTTPAsyncTask2().execute("https://6ghfrrqsb3.execute-api.ap-south-1.amazonaws.com/Dev/login",object.toString());
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
            if(responseObject.getString("message").equals("Success"))
            {

            }
            else
                Toast.makeText(getApplication(),"wrong credentials",Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }    }


}}