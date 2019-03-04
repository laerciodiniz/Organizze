package com.example.laercio.organizze.ui.Receitas;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laercio.organizze.R;
import com.example.laercio.organizze.config.ConfiguracaoFirebase;
import com.example.laercio.organizze.model.Movimentacao;
import com.example.laercio.organizze.model.Usuario;
import com.example.laercio.organizze.utils.Base64Custom;
import com.example.laercio.organizze.utils.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceitasActivity extends AppCompatActivity {

    @BindView(R.id.edtData)
    TextInputEditText campoData;
    @BindView(R.id.edtCategoria)
    TextInputEditText campoCategoria;
    @BindView(R.id.edtDescricao)
    TextInputEditText campoDescricao;
    @BindView(R.id.edtValor)
    EditText campoValor;

    private Movimentacao movimentacao;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        ButterKnife.bind(this);

        //Preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());

        recuperarReceitaTotal();

    }

    public void salvarReceita(View view){

        if ( validarCamposReceitas() ) {

            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble( campoValor.getText().toString() );

            movimentacao.setValor( valorRecuperado );
            movimentacao.setCategoria( campoCategoria.getText().toString() );
            movimentacao.setDescricao( campoDescricao.getText().toString() );
            movimentacao.setData( data );
            movimentacao.setTipo("r");

            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita( receitaAtualizada );

            movimentacao.salvar( data );

            finish();
        }

    }

    public Boolean validarCamposReceitas(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if ( !textoValor.isEmpty() ){
            if ( !textoData.isEmpty() ){
                if ( !textoCategoria.isEmpty() ){
                    if ( !textoDescricao.isEmpty() ){
                        return true;
                    }else {
                        Toast.makeText(ReceitasActivity.this,
                                "Descrição não foi preenchido!",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else {
                    Toast.makeText(ReceitasActivity.this,
                            "Categoria não foi preenchido!",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }else {
                Toast.makeText(ReceitasActivity.this,
                        "Data não foi preenchido!",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }else {
            Toast.makeText(ReceitasActivity.this,
                    "Valor não foi preenchido!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void recuperarReceitaTotal(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child( idUsuario );

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue( Usuario.class );
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("testelaercio","ERROOUVINTE:" + databaseError);
            }
        });
    }

    public void atualizarReceita(Double receita){

        //Recupera o usuario do Firebase
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child( idUsuario );

        //Atualiza o valor do campo no Firebase
        usuarioRef.child("receitaTotal").setValue( receita );

    }

}
