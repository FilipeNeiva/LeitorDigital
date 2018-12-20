package com.example.filipeneiva.testbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

   private final static int SOLICITA_ATIVACAO = 1;
   private final static int SOLICITA_CONEXAO = 2;

    Button btnConeccao;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice meuDevice;
    BluetoothSocket meuSocket;
    Boolean coneccao = false;

    UUID meuUuid= UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView splini;
        splini = findViewById(R.id.splini);
        btnConeccao = findViewById(R.id.conectar);



        if (mBluetoothAdapter == null) {
            splini.setText("Deu ruim!!");
            finish();
        }else if(!mBluetoothAdapter.isEnabled()) {

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, SOLICITA_ATIVACAO);
        }

        btnConeccao.setOnClickListener((v) -> {
            if(coneccao){
                try{
                    meuSocket.close();
                    coneccao = false;
                    btnConeccao.setText("Conectar");
                    Toast.makeText(this, "Bluetooth desconectado", Toast.LENGTH_SHORT).show();
                }catch (IOException erro){
                    Toast.makeText(this, "Erro: " + erro, Toast.LENGTH_SHORT).show();
                }
            }else {
                startActivityForResult(new Intent(MainActivity.this, ConectarDispositivo.class), SOLICITA_CONEXAO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SOLICITA_ATIVACAO:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "Bluetooth ativado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Vai se ferrar então", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case SOLICITA_CONEXAO:
                if(resultCode == Activity.RESULT_OK) {
                    String mac = data.getExtras().getString(ConectarDispositivo.enderecoMAC);

                    meuDevice = mBluetoothAdapter.getRemoteDevice(mac);

                    try{
                        meuSocket = meuDevice.createRfcommSocketToServiceRecord(meuUuid);

                        meuSocket.connect();

                        coneccao = true;

                        btnConeccao.setText("Desconectar");

                        Toast.makeText(this, "Conectado com sucesso com: " + mac, Toast.LENGTH_SHORT).show();
                    } catch (IOException erro){
                        coneccao = false;
                        Toast.makeText(this, "Erro: " + erro, Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(this, "Mac: " + mac, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Falha ao obter o MAc", Toast.LENGTH_SHORT).show();

                }

        }
    }

}
