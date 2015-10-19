package uk.ac.kcl.odo.mobileminer.activities;

import edu.mit.media.openpds.client.PersonalDataStore;
import edu.mit.media.openpds.client.PreferencesWrapper;
import edu.mit.media.openpds.client.RegistryClient;
import edu.mit.media.openpds.client.UserLoginTask;
import edu.mit.media.openpds.client.UserRegistrationTask;
import uk.ac.kcl.odo.mobileminer.R;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class PdsRegisterActivity extends BaseActivity {
	
	PreferencesWrapper prefs;
	PersonalDataStore pds;
	RegistryClient registryClient;
	
	EditText userText, emailText, pwText, pwRepText;
	String userName, email, pw, pwRep;
	
	String basicAuth = "Basic NmZiYWQ2MGVmYTdiMWYyOWUyZWUyYTNiZDg2MjE2OjA1Yjg4YzlhYmVmOGNkZGUwZGM3YjdhNWJkMGY3Ng==";
	String clientKey = "6fbad60efa7b1f29e2ee2a3bd86216";
	String clientSecret = "05b88c9abef8cdde0dc7b7a5bd0f76";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        
		prefs = new PreferencesWrapper(this);
			
		String registryUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("mobileminer_openpds_registry","http://10.0.2.2:8000");
				
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pds_register);
        
        userText = (EditText) findViewById(R.id.pdsuser);
        emailText = (EditText) findViewById(R.id.pdsemail);
        pwText = (EditText) findViewById(R.id.pdspassword);
        pwRepText = (EditText) findViewById(R.id.pdspasswordrep);
                            
        registryClient = new RegistryClient(registryUrl, clientKey,clientSecret, "funf_write",basicAuth);
               
	}
	
	private void getRegistrationDetails() {
		//userName = userText.getText().toString();
		//email = emailText.getText().toString();
		//pw = pwText.getText().toString();
		//pwRep = pwRepText.getText().toString();
		
		userName = "Bob Smith";
		email = "bob@smith.org";
		pw = "bob";
		pwRep = "bob";
		
	}
	
	public void pdsRegister(View button) {
		getRegistrationDetails();
		
		UserRegistrationTask userRegistrationTask = new UserRegistrationTask(this, prefs, registryClient) {
		@Override
		protected void onComplete() {
			//textView.setText("Registration Succeeded");
		}
		
		@Override
		protected void onError() {
			//textView.setText("An error occurred while registering");
		}
	};
	userRegistrationTask.execute("Bob Smith", "bob@smith.org", "bob");
}
	
	public void pdsLogin(View button) {
		getRegistrationDetails();
		UserLoginTask userLoginTask = new UserLoginTask(this,  prefs, registryClient) {
			@Override
			protected void onComplete() {
				//textView.setText("Login Succeeded");
				try {
					pds = new PersonalDataStore(PdsRegisterActivity.this);

				} catch (Exception e) {
					Log.w("HelloPDS", "Unable to construct PDS after login");
				}
			}
			
			@Override
			protected void onError() {
				//textView.setText("An error occurred while logging in");
				
				// If an error occurred, maybe the user hasn't been registered yet?
				// For cases where auto-registration is desired, call UserRegistrationTask here
			}
		};
		userLoginTask.execute("bob@smith.org", "bob");
	}
	
	

}