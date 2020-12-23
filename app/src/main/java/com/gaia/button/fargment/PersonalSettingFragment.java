package com.gaia.button.fargment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gaia.button.GaiaApplication;
import com.gaia.button.R;
import com.gaia.button.activity.AboutActivity;
import com.gaia.button.activity.AccountActivity;
import com.gaia.button.activity.CustomerActivity;
import com.gaia.button.activity.LoginMainActivity;
import com.gaia.button.adapter.PersonalSettingAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.PersonalDeviceModel;
import com.gaia.button.model.PersonalSettingModel;
import com.gaia.button.net.NetConfig;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

public class PersonalSettingFragment extends BaseFragment implements PersonalSettingAdapter.OnItemListener, IUserListener {
    private View mRootView;
//    private RecyclerView mRecyclerView;
//    private List<PersonalSettingModel> mList = new ArrayList<>();
//    private PersonalSettingAdapter mSettingAdapter;
    private TextView mTvLoginout;
    private ConstraintLayout mCLAbout,mClCustomer,mClAccount;
    private ImageView mAutoPlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_personal_setting, container, false);
            initView();
        }
        return mRootView;
    }
    private void initView(){
        mTvLoginout = mRootView.findViewById(R.id.tv_login_out);
        mTvLoginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetConfig.isGet = true;
                UserManager.getRequestHandler().requestLoginOut(PersonalSettingFragment.this);
            }
        });
        mCLAbout = mRootView.findViewById(R.id.cl_about);
        mCLAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });
        mClCustomer = mRootView.findViewById(R.id.cl_kehu);
        mClCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerActivity.class));

            }
        });
        mClAccount = mRootView.findViewById(R.id.cl_account_info);
        mClAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AccountActivity.class));
            }
        });
        mAutoPlay = mRootView.findViewById(R.id.iv_auto_play);
        mAutoPlay.setSelected(PreferenceManager.getInstance().getAutoPlay(PreferenceManager.getInstance().getAccountInfo().getUserID()));
        mAutoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAutoPlay.isSelected()){
                    mAutoPlay.setSelected(false);
                }else{
                    mAutoPlay.setSelected(true);

                }
                PreferenceManager.getInstance().setAutoPlayString(PreferenceManager.getInstance().getAccountInfo().getUserID(),mAutoPlay.isSelected());
            }
        });
    }


    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if (requestTag == ConstantUtil.Net_Tag_LogOut) {
            PreferenceManager.getInstance().setLoginOut();
            GaiaApplication.getInstance().clearActivities();
            startActivity(new Intent(getActivity(), LoginMainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {

    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
