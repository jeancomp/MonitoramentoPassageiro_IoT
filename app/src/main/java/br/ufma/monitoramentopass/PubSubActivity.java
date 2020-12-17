package br.ufma.monitoramentopass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import br.ufma.lsdi.cddl.CDDL;
import br.ufma.lsdi.cddl.message.Message;
import br.ufma.lsdi.cddl.pubsub.Publisher;
import br.ufma.lsdi.cddl.pubsub.PublisherFactory;
import br.ufma.lsdi.cddl.pubsub.Subscriber;
import br.ufma.lsdi.cddl.pubsub.SubscriberFactory;

public class PubSubActivity extends AppCompatActivity {

    private static final String MY_SERVICE = "Location";
    private EditText mensagemEditText;
    private TextView mensagensTextView;
    private CDDL cddl;
    private Publisher pub;
    private Subscriber sub;
    private EventBus eb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_sub);

        mensagemEditText = findViewById(R.id.mensagemEditText);

        mensagensTextView = findViewById(R.id.mensagensTextView);

        Button publicarButton = findViewById(R.id.publicarButton);
        publicarButton.setOnClickListener(this::onClick);

        eb = EventBus.builder().build();
        eb.register(this);


        configCDDL();
        configPublisher();
        configSubscriber();

    }

    private void configPublisher() {
        pub = PublisherFactory.createPublisher();
        pub.addConnection(cddl.getConnection());
    }

    private void configSubscriber() {
        sub = SubscriberFactory.createSubscriber();
        sub.addConnection(cddl.getConnection());

        sub.subscribeServiceByName(MY_SERVICE);
        sub.setSubscriberListener(this::onMessage);

    }

    private void onMessage(Message message) {
        eb.post(new MessageEvent(message));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on(MessageEvent event) {
        Object[] valor = event.getMessage().getServiceValue();
        mensagensTextView.setText((String) valor[0]);
    }


    private void configCDDL() {
        cddl = CDDL.getInstance();
    }

    @Override
    protected void onDestroy() {
        eb.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        AppMenu appMenu = AppMenu.getInstance();
        appMenu.setMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AppMenu appMenu = AppMenu.getInstance();
        appMenu.setMenuItem(PubSubActivity.this, item);
        return super.onOptionsItemSelected(item);
    }

    private void onClick(View view) {
        Message msg = new Message();
        msg.setServiceName(MY_SERVICE);
        msg.setServiceValue(mensagemEditText.getText().toString());
        pub.publish(msg);
    }

}
