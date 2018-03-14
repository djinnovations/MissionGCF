package com.example.test.spplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.spplayer.R;
import com.example.test.spplayer.uiutils.UiRandomUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 14-03-2018.
 */

public abstract class SingleMenuFragment extends BaseFragment {


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_menu_single;
    }

    public abstract boolean isAddSnapper();


    public
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    public
    @BindView(R.id.llBody)
    LinearLayout llBody;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUpRecycleView();
    }


    public
    @BindView(R.id.rvMenu)
    RecyclerView rvMenu;
    private void setUpRecycleView() {
        RecyclerView.LayoutManager mLayoutManager = /*new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false)*/
                getLayoutManager();
        rvMenu.setHasFixedSize(false);
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.setItemAnimator(new DefaultItemAnimator());
        if (isAddSnapper())
            UiRandomUtils.getInstance().addSnapper(rvMenu, Gravity.START);
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter == null)
            return;
        rvMenu.setAdapter(adapter);
    }

    public abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract RecyclerView.Adapter getAdapter();

    public abstract void changeData(List dataList);
}
