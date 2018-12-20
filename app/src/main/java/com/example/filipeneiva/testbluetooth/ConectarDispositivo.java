package com.example.filipeneiva.testbluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

import static android.content.ContentValues.TAG;

public class ConectarDispositivo extends ListActivity{

    static String enderecoMAC = null;
    private Bluetooth bluetoothsNaoPareados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            bluetoothsNaoPareados = Bluetooth.startFindDevices(this, (Bluetooth.BluetoothListener) this);
        } catch (Exception e) {
            Log.e(TAG, "Erro: ", e);
        }


        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> dispositivosPareados = bluetooth.getBondedDevices();
        //ArrayList<BluetoothDevice> dispositivosNaoPareados = bluetoothsNaoPareados.getDispositivos();

        if (dispositivosPareados.size() != 0){
            for(BluetoothDevice dispositivo: dispositivosPareados){
                String nomeBt = dispositivo.getName();
                String macBt = dispositivo.getAddress();
                arrayBluetooth.add(nomeBt + "\n" + macBt);
            }
        }
        setListAdapter(arrayBluetooth);

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacaoGeral = ((TextView) v).getText().toString();
        String enderecoMac = informacaoGeral.substring(informacaoGeral.length() - 17);

        setResult(RESULT_OK, new Intent().putExtra(enderecoMAC, enderecoMac));
        finish();
    }


}

