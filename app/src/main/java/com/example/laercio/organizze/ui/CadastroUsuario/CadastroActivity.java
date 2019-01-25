package com.example.laercio.organizze.ui.CadastroUsuario;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laercio.organizze.R;
import com.example.laercio.organizze.config.ConfiguracaoFirebase;
import com.example.laercio.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CadastroActivity extends AppCompatActivity {

    @BindView(R.id.editNome)
    TextView campoNome;

    @BindView(R.id.editEmail)
    TextView campoEmail;

    @BindView(R.id.editSenha)
    TextView campoSenha;

    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.buttonCadastrar)
    public void buttonCadastrar(View view){
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        //validar se os campos foram preenchidos
        if (!textoNome.isEmpty()){
            if (!textoEmail.isEmpty()){
                if (!textoSenha.isEmpty()){

                    usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    cadastrarUsuario();

                }else{
                    campoSenha.setError("Preencha a senha!");
                    Toast.makeText(CadastroActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_LONG).show();
                }
            }else{
                campoEmail.setError("Preencha o e-mail!");
                Toast.makeText(CadastroActivity.this,
                        "Preencha o e-mail!",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            campoNome.setError("Preencha o nome!");
            Toast.makeText(CadastroActivity.this,
                    "Preencha o nome!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,
                            "Sucesso ao cadastrar usuario!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Erro ao cadastrar usuario!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
