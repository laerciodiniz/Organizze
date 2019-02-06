package com.example.laercio.organizze.ui.CadastroUsuario;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laercio.organizze.R;
import com.example.laercio.organizze.config.ConfiguracaoFirebase;
import com.example.laercio.organizze.model.Usuario;
import com.example.laercio.organizze.utils.Base64Custom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

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

        /*campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        buttonCadastro = findViewById(R.id.buttonCadastrar);*/

        /*buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });*/
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

                    String idUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setIdUsuario( idUsuario );
                    usuario.salvar();
                    finish();

                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um e-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
