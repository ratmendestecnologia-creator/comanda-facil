package br.com.comandafacil;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
    private LinearLayout root, header, setupPanel;
    private WebView webView;
    private EditText addressInput;
    private TextView status;
    private SharedPreferences prefs;
    private String serverUrl = "";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("comanda_facil", MODE_PRIVATE);
        serverUrl = prefs.getString("server_url", "");
        buildScreen();
        if (!serverUrl.isEmpty()) connect(serverUrl);
    }

    private GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(radius); return d;
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD); return t;
    }
    private Button button(String label) {
        Button b = new Button(this); b.setText(label); b.setAllCaps(false); b.setTextColor(Color.WHITE);
        b.setBackground(bg(Color.rgb(22,119,184), 18)); return b;
    }
    private void buildScreen() {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(Color.rgb(245,248,251));
        header = new LinearLayout(this); header.setGravity(Gravity.CENTER_VERTICAL); header.setPadding(16,8,12,8); header.setBackgroundColor(Color.rgb(16,42,67));
        TextView brand = text("Comanda Fácil", 20, Color.WHITE, true); header.addView(brand, new LinearLayout.LayoutParams(0, dp(48), 1));
        Button change = button("Servidor"); change.setOnClickListener(v -> showSetup()); header.addView(change);
        root.addView(header);

        setupPanel = new LinearLayout(this); setupPanel.setOrientation(LinearLayout.VERTICAL); setupPanel.setPadding(24,32,24,24);
        TextView title = text("Conectar ao restaurante", 23, Color.rgb(16,42,67), true); setupPanel.addView(title);
        TextView help = text("Digite o endereço exibido no computador onde o Comanda Fácil está instalado. O celular e o computador precisam estar no mesmo Wi-Fi.", 15, Color.DKGRAY, false);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1,-2); hp.topMargin=dp(12); setupPanel.addView(help,hp);
        addressInput = new EditText(this); addressInput.setSingleLine(true); addressInput.setText(serverUrl.isEmpty()?"http://192.168.0.100:3000":serverUrl); addressInput.setHint("http://IP-DO-COMPUTADOR:3000"); addressInput.setInputType( android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_URI);
        LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(-1,dp(54)); ep.topMargin=dp(20); setupPanel.addView(addressInput,ep);
        Button connect = button("Conectar"); LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1,dp(52)); cp.topMargin=dp(14); setupPanel.addView(connect,cp);
        connect.setOnClickListener(v -> connect(addressInput.getText().toString().trim()));
        status = text("Aguardando endereço do servidor.", 13, Color.DKGRAY, false); LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1,-2); sp.topMargin=dp(14); setupPanel.addView(status,sp);
        root.addView(setupPanel,new LinearLayout.LayoutParams(-1,-1));

        webView = new WebView(this); webView.setVisibility(View.GONE); webView.getSettings().setJavaScriptEnabled(true); webView.getSettings().setDomStorageEnabled(true); webView.getSettings().setDatabaseEnabled(true); webView.getSettings().setLoadsImagesAutomatically(true); webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) { return false; }
            @Override public void onPageFinished(WebView view, String url) { status.setText("Conectado: " + serverUrl); }
            @Override public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) { if(request.isForMainFrame()) Toast.makeText(MainActivity.this,"Não foi possível acessar o servidor. Confira o Wi-Fi e o endereço.",Toast.LENGTH_LONG).show(); }
        });
        root.addView(webView,new LinearLayout.LayoutParams(-1,0,1));
        setContentView(root);
    }
    private void showSetup() {
        webView.setVisibility(View.GONE); setupPanel.setVisibility(View.VISIBLE);
        addressInput.setText(serverUrl.isEmpty()?"http://192.168.0.100:3000":serverUrl);
    }
    private void connect(String raw) {
        String url=raw;
        if(url.isEmpty()) { Toast.makeText(this,"Informe o endereço do computador.",Toast.LENGTH_SHORT).show(); return; }
        if(!url.startsWith("http://") && !url.startsWith("https://")) url="http://"+url;
        while(url.endsWith("/")) url=url.substring(0,url.length()-1);
        if(!android.util.Patterns.WEB_URL.matcher(url).matches()) { Toast.makeText(this,"Endereço inválido. Exemplo: http://192.168.0.10:3000",Toast.LENGTH_LONG).show(); return; }
        serverUrl=url; prefs.edit().putString("server_url",url).apply();
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(addressInput.getWindowToken(),0);
        setupPanel.setVisibility(View.GONE); webView.setVisibility(View.VISIBLE); status.setText("Conectando..."); webView.loadUrl(serverUrl);
    }
    private int dp(int v) { return (int)(v*getResources().getDisplayMetrics().density+0.5f); }
    @Override public void onBackPressed() {
        if(webView.getVisibility()==View.VISIBLE && webView.canGoBack()) webView.goBack();
        else if(webView.getVisibility()==View.VISIBLE) showSetup(); else super.onBackPressed();
    }
}
