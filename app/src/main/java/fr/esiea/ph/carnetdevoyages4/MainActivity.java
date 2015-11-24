package fr.esiea.ph.carnetdevoyages4;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText login = (EditText) findViewById(R.id.user_email);
        final EditText pass = (EditText) findViewById(R.id.user_password);
        final Button loginButton = (Button) findViewById(R.id.connect);
        //Si un des deux champs est vide, alors on affiche l'erreurs
        final String loginTxt = login.getText().toString();
        final String passTxt = pass.getText().toString();
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String loginTxt = login.getText().toString();
                final String passTxt = pass.getText().toString();
                // On déclare le pattern que l’on doit vérifier
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                // On déclare un matcher, qui comparera le pattern avec la
                // string passée en argument
                Matcher m = p.matcher(loginTxt);
                // Si l’adresse mail saisie ne correspond au format d’une
                // adresse mail on un affiche un message à l'utilisateur
                if (!m.matches()) {
                    // Toast est une classe fournie par le SDK Android
                    // pour afficher les messages (indications) à l'intention de
                    // l'utilisateur. Ces messages ne possédent pas d'interaction avec l'utilisateur
                    // Le premier argument représente le contexte, puis
                    // le message et à la fin la durée d'affichage du Toast (constante
                    // LENGTH_SHORT ou LENGTH_LONG). Sans oublier d'appeler la méthode
                    //show pour afficher le Toast
                    Toast.makeText(MainActivity.this, R.string.email_format_error,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this,
                        Nav_drawer_Activity.class);
                intent.putExtra(EXTRA_LOGIN, loginTxt);
                intent.putExtra(EXTRA_PASSWORD, passTxt);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
