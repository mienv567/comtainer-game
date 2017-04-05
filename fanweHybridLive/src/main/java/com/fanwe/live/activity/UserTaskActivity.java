package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.UserTaskItem;
import com.fanwe.hybrid.model.UserTaskModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.customview.SDViewPager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.fragment.UserTaskFragment;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class UserTaskActivity extends BaseTitleActivity {

    private static final int NOT_COMPLETED_TASK = 0;
    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;
    @ViewInject(R.id.ll_tab_main)
    private LinearLayout ll_tab_main;
    @ViewInject(R.id.ll_tab_day)
    private LinearLayout ll_tab_day;
    @ViewInject(R.id.ll_tab_other)
    private LinearLayout ll_tab_other;
    @ViewInject(R.id.iv_task_main)
    private ImageView iv_task_main;
    @ViewInject(R.id.iv_task_day)
    private ImageView iv_task_day;
    @ViewInject(R.id.iv_task_other)
    private ImageView iv_task_other;
    private UserTaskFragment mMainTaskFragment;
    private UserTaskFragment mDayTaskFragment;
    private UserTaskFragment mOtherTaskFragment;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<UserTaskItem> mMainTasks = new ArrayList<>();
    private List<UserTaskItem> mDayTasks = new ArrayList<>();
    private List<UserTaskItem> mOtherTasks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_task);
        initTitle();
        initSDViewPager();
        initListener();
    }

    private void initSDViewPager() {
        mMainTaskFragment = new UserTaskFragment();
        mDayTaskFragment = new UserTaskFragment();
        mOtherTaskFragment = new UserTaskFragment();
        mFragments.add(mMainTaskFragment);
        mFragments.add(mDayTaskFragment);
        mFragments.add(mOtherTaskFragment);
        vpg_content.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        });
        vpg_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                showCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vpg_content.setOffscreenPageLimit(3);
    }

    private void initListener(){
        ll_tab_main.setOnClickListener(new TabOnClickListenr());
        ll_tab_day.setOnClickListener(new TabOnClickListenr());
        ll_tab_other.setOnClickListener(new TabOnClickListenr());
        ll_tab_main.performClick();
    }

    private class TabOnClickListenr implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_tab_main:
                    jumpToFragment(mMainTaskFragment);
                    break;
                case R.id.ll_tab_day:
                    jumpToFragment(mDayTaskFragment);
                    break;
                case R.id.ll_tab_other:
                    jumpToFragment(mOtherTaskFragment);
                    break;
            }
        }
    }

    private void cleanAllTab(){
        iv_task_main.setImageDrawable(getResources().getDrawable(R.drawable.ic_task_main_gray));
        iv_task_day.setImageDrawable(getResources().getDrawable(R.drawable.ic_task_day_gray));
        iv_task_other.setImageDrawable(getResources().getDrawable(R.drawable.ic_task_other_gray));
    }

    private void showCurrentTab(int index){
        cleanAllTab();
        switch (index){
            case 0:
                iv_task_main.setImageDrawable(getResources().getDrawable(R.drawable.ic_task_main_light));
                break;
            case 1:
                iv_task_day.setImageDrawable(getResources().getDrawable(R.drawable.ic_task_day_light));
                break;
            case 2:
                iv_task_other.setImageDrawable(getResources().getDrawable(R.drawable.ic_task_other_light));
                break;
        }
    }

    private void jumpToFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        int index = mFragments.indexOf(fragment);
        showCurrentTab(index);
        vpg_content.setCurrentItem(index);
    }

    @Override
    protected void initSystemBar() {
        super.initSystemBar();
        SDViewUtil.setStatusBarTintResource(this, R.color.main_color);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.my_task));
        mTitle.setBackgroundColor(getResources().getColor(R.color.main_color));
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setImageRight(R.drawable.ic_task_more);
    }

    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        Intent intent = new Intent(UserTaskActivity.this, UserCompletedActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    public void requestData() {
        CommonInterface.requestMyTask(NOT_COMPLETED_TASK, new AppRequestCallback<UserTaskModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (rootModel.isOk()) {
                    divideTasks(actModel.getMain_task());
                    mMainTaskFragment.setData(mMainTasks);
                    mDayTaskFragment.setData(mDayTasks);
                    mOtherTaskFragment.setData(mOtherTasks);
                }
            }
        });
    }

    private void clearAllTasks(){
        mMainTasks.clear();
        mDayTasks.clear();
        mOtherTasks.clear();
    }

    private void divideTasks(List<UserTaskItem> tasks){
        if(tasks != null && tasks.size() > 0){
            clearAllTasks();
            for(UserTaskItem item : tasks){
                switch (item.getTask_type()){
                    case UserTaskItem.TYPE_MAIN:
                        mMainTasks.add(item);
                        break;
                    case UserTaskItem.TYPE_DAY:
                        mDayTasks.add(item);
                        break;
                    case UserTaskItem.TYPE_OTHER:
                        mOtherTasks.add(item);
                        break;
                }
            }
        }
    }



    /**
     * 初始化数据
     */
    private List<UserTaskItem> initData() {
        List<UserTaskItem> list = new ArrayList<>();
        UserTaskItem item0 = new UserTaskItem();
        item0.setTask_id("0");
        item0.setTask_title("等级达到5级（主线）");
        List<String> experience0 = new ArrayList<>();
        experience0.add("40经验");
        item0.setTask_experience(experience0);
        item0.setTask_type(0);
        item0.setTask_state(0);
        item0.setSub_task(initSubList());
        UserTaskItem item1 = new UserTaskItem();
        item1.setTask_id("0");
        item1.setTask_title("主播点亮一次（日常）");
        List<String> experience1 = new ArrayList<>();
        experience1.add("10经验");
        item1.setTask_experience(experience1);
        item1.setTask_type(0);
        item1.setTask_state(0);
        item1.setTask_to_do(0);
        UserTaskItem item2 = new UserTaskItem();
        item2.setTask_id("0");
        item2.setTask_title("给主播赠送一个礼物（日常）");
        List<String> experience2 = new ArrayList<>();
        experience2.add("100经验");
        item2.setTask_experience(experience2);
        item2.setTask_type(0);
        item2.setTask_state(1);
        UserTaskItem item3 = new UserTaskItem();
        item3.setTask_id("0");
        item3.setTask_title("充任意金额钻石（其他）");
        List<String> experience3 = new ArrayList<>();
        List<String> detail = new ArrayList<>();
        detail.add("1.单次充值超过100麻辣币，可获得一台iphone");
        detail.add("2.此任务每个月可以完成3次");
        experience3.add("铁公鸡的毛徽章");
        item3.setTask_detail("1.单次充值超过100麻辣币，可获得一台iphone");
        item3.setTask_experience(experience3);
        item3.setTask_type(0);
        item3.setTask_state(0);
        item3.setTask_to_do(0);
        list.add(item0);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        return list;
    }

    private List<UserTaskItem> initSubList() {
        List<UserTaskItem> subList = new ArrayList<>();
        UserTaskItem sub0 = new UserTaskItem();
        sub0.setTask_id("0");
        sub0.setTask_title("单词充值100麻辣币");
        List<String> experience_sub0 = new ArrayList<>();
        experience_sub0.add("10经验");
        sub0.setTask_experience(experience_sub0);
        sub0.setTask_type(0);
        sub0.setTask_state(0);
        sub0.setTask_to_do(1);
        List<String> detail = new ArrayList<>();
        detail.add("1.单次充值超过100麻辣币，可获得一台iphone");
        detail.add("2.此任务每个月可以完成3次");
        sub0.setTask_detail("1.单次充值超过100麻辣币，可获得一台iphone");
        UserTaskItem sub1 = new UserTaskItem();
        sub1.setTask_id("0");
        sub1.setTask_title("单词充值100麻辣币");
        List<String> experience_sub1 = new ArrayList<>();
        experience_sub1.add("40经验");
        sub1.setTask_experience(experience_sub1);
        sub1.setTask_type(0);
        sub1.setTask_state(0);
        sub1.setTask_to_do(2);
        UserTaskItem sub2 = new UserTaskItem();
        sub2.setTask_id("0");
        sub2.setTask_title("单词充值100麻辣币");
        List<String> experience_sub2 = new ArrayList<>();
        experience_sub2.add("30经验");
        sub2.setTask_experience(experience_sub2);
        sub2.setTask_type(0);
        sub2.setTask_state(2);
        UserTaskItem sub3 = new UserTaskItem();
        sub3.setTask_id("0");
        sub3.setTask_title("单词充值100麻辣币");
        List<String> experience_sub3 = new ArrayList<>();
        experience_sub3.add("20经验");
        sub3.setTask_experience(experience_sub3);
        sub3.setTask_type(0);
        sub3.setTask_state(0);
        List<String> detail1 = new ArrayList<>();
        detail1.add("1.单次充值超过100麻辣币，可获得一台iphone");
        detail1.add("2.此任务每个月可以完成3次");
        sub3.setTask_detail("1.单次充值超过100麻辣币，可获得一台iphone");
        subList.add(sub0);
        subList.add(sub1);
        subList.add(sub2);
        subList.add(sub3);
        return subList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mAdapter != null) {
//            if (mAdapter.hasTaskCanGet()) {
//                App.mShowTaskRedPoint = true;
//            } else {
//                App.mShowTaskRedPoint = false;
//            }
//            SDEventManager.post(new ERedPointChange());
//        }
    }
}
