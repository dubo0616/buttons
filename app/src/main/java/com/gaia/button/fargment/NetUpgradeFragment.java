package com.gaia.button.fargment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gaia.button.R;

import java.io.File;

/**
 * 2021/3/23 11:07
 */

public class NetUpgradeFragment extends Fragment implements View.OnClickListener{
    private TextView tvOnlineVersion, tvNowVersion;
    private Button btnStartUpgrade;
    private String onLineVersion, deviceVersion, deviceName, downUrl;
    private NetUpgradeFragmentListener netUpgradeFragmentListener;
    public static NetUpgradeFragment newInstance() {
        return new NetUpgradeFragment();
    }
    public NetUpgradeFragment() {
    }

    // This event fires first, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override // Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NetUpgradeFragment.NetUpgradeFragmentListener) {
            this.netUpgradeFragmentListener = (NetUpgradeFragment.NetUpgradeFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade_net, container, false);
        init(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.btnStartDownUpgrade:
                if(null != netUpgradeFragmentListener){
                    netUpgradeFragmentListener.onStartDownUpgrade();
                }
                break;
        }
    }
    private void init(View view){
        tvOnlineVersion = view.findViewById(R.id.tvOnlineVersion);
        tvNowVersion = view.findViewById(R.id.tvNowVersion);
        btnStartUpgrade = view.findViewById(R.id.btnStartDownUpgrade);
        Bundle bundle = getArguments();
        onLineVersion = bundle.getString("onLineVersion");
        deviceVersion = bundle.getString("deviceVersion");
        downUrl = bundle.getString("downUrl");
        deviceName = bundle.getString("upgradeDevice");
        tvOnlineVersion.setText(getString(R.string.onLineVersion) + " " + onLineVersion);
        tvNowVersion.setText(getString(R.string.nowVersion) + " " + deviceVersion);
        btnStartUpgrade.setOnClickListener(this);
    }
    public interface NetUpgradeFragmentListener {
        /**
         * <p>Called when a file is picked by the user.</p>
         *
         *          The file which had been selected by the user.
         */
        void onStartDownUpgrade();

    }
}
