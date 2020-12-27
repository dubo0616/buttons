package com.gaia.button.fargment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.gaia.button.activity.WebViewActivity;
import com.gaia.button.adapter.PersonalSettingAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AutoplayModel;
import com.gaia.button.model.PersonalDeviceModel;
import com.gaia.button.model.PersonalSettingModel;
import com.gaia.button.model.UpdateModel;
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
    private ConstraintLayout mCLAbout,mClCustomer,mClAccount,mClUpdate,mClPrivate,mClUser;
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

        mClUpdate = mRootView.findViewById(R.id.cl_update);
        mClUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserManager.getRequestHandler().requestUpdate(PersonalSettingFragment.this,getVersion());
            }
        });
        mAutoPlay = mRootView.findViewById(R.id.iv_auto_play);
        mAutoPlay.setSelected(PreferenceManager.getInstance().getAccountInfo().getAutoplay() ==1);
        mAutoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAutoPlay.isSelected()){
                    mAutoPlay.setSelected(false);
                }else{
                    mAutoPlay.setSelected(true);

                }
                UserManager.getRequestHandler().requestSetAutoPlay(PersonalSettingFragment.this,mAutoPlay.isSelected()?1:2);

            }
        });
        mClPrivate = mRootView.findViewById(R.id.cl_yinsi);
        mClPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL_KEY,ConstantUtil.PRIVATE_URL);
                intent.putExtra(WebViewActivity.TITLE_KEY,"隐私条款");
                startActivity(intent);
            }
        });
        mClUser = mRootView.findViewById(R.id.cl_xieyi);
        mClUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL_KEY,ConstantUtil.USER_URL);
                intent.putExtra(WebViewActivity.TITLE_KEY,"使用协议");
                startActivity(intent);
            }
        });
    }
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersion() {
        if(getActivity() == null){
            return "1.0";
        }
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }

    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if (requestTag == ConstantUtil.Net_Tag_LogOut) {
            PreferenceManager.getInstance().setLoginOut();
            GaiaApplication.getInstance().clearActivities();
            Intent intent = new Intent(getActivity(), LoginMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }else if(requestTag == ConstantUtil.Net_Tag_User_AUTOPLAY){
            AutoplayModel model = (AutoplayModel) data;
            if(model != null){
                PreferenceManager.getInstance().getAccountInfo().setAutoplay(model.getAutoplay());
                PreferenceManager.getInstance().setIntValue(PreferenceManager.ACC_AUTO_PLAY,model.getAutoplay());
            }
            displayShortToast("设置成功");
        }else if(requestTag == ConstantUtil.Net_Tag_User_GetVersion){
            UpdateModel model = (UpdateModel) data;
            if(model.getIsUpdate() == 1 && !TextUtils.isEmpty(model.getUrl())){
                Uri uri = Uri.parse(model.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }else {
                displayShortToast("已经是最新版本");
            }

        }
    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        if (requestTag == ConstantUtil.Net_Tag_LogOut) {
            displayShortToast("退出登录失败");
        }else if(requestTag == ConstantUtil.Net_Tag_User_AUTOPLAY){
            displayShortToast("设置失败");
        } else if(requestTag == ConstantUtil.Net_Tag_User_GetVersion){
            displayShortToast("网络异常");
        }
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
