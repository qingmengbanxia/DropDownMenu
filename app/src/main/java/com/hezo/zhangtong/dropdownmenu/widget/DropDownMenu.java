package com.hezo.zhangtong.dropdownmenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hezo.zhangtong.dropdownmenu.R;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class DropDownMenu extends LinearLayout {

    //顶部菜单布局
    private LinearLayout tabMenuView;

    //底部容器 包过内容区域 遮罩区域 菜单弹出区域
    private FrameLayout containerView;
    //菜单弹出区域
    private FrameLayout popupMenuViews;
    //遮罩区域
    private View maskView;
    //背景图片
    private ImageView contentView;
    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //文本选中的颜色
    private int textSelectedColor = 0xff890c85;
    //文本未被选中的颜色
    private int textUnSelectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //菜单背景颜色
    private int menuBackgroundColor = 0xffffffff;
    //水平分割线颜色
    private int underlineColor = 0xffcccccc;

    //字体大小
    private int menuTextSize = 14;
    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnSelectedIcon;

    //菜单项被选中位置 初始没有菜单被选中记为-1
    private int currentTabPosition = -1;

    public DropDownMenu(Context context) {
        this(context, null);
    }

    public DropDownMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.dropDownMenu);
        underlineColor = a.getColor(R.styleable.dropDownMenu_underlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.dropDownMenu_dividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.dropDownMenu_textSelectColor, textSelectedColor);
        textUnSelectedColor = a.getColor(R.styleable.dropDownMenu_textUnSelectColor, textUnSelectedColor);
        menuBackgroundColor = a.getColor(R.styleable.dropDownMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.dropDownMenu_maskColor, maskColor);

        menuTextSize = a.getDimensionPixelSize(R.styleable.dropDownMenu_menuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.dropDownMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnSelectedIcon = a.getResourceId(R.styleable.dropDownMenu_menuUnSelectedIcon, menuUnSelectedIcon);
        a.recycle();

        initViews(context);
    }

    private void initViews(Context context) {
        //创建顶部菜单指示
        tabMenuView = new LinearLayout(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setLayoutParams(lp);
        addView(tabMenuView, 0);

        //创建下划线
        View underLineView = new View(context);
        underLineView.setLayoutParams(new LayoutParams(MATCH_PARENT, dp2Px(1f)));
        underLineView.setBackgroundColor(underlineColor);
        addView(underLineView, 1);

        //初始化 contentView
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        addView(containerView, 2);

    }

    private int dp2Px(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm);
    }

    /**
     * 初始化 DropDownMenu 显示具体的内容
     */
    public void setDropDownMenu(List<String> tabTexts, List<View> popupViews, ImageView contentView)
            throws IllegalAccessException {

        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalAccessException("tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        this.contentView = contentView;
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT,
                MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        maskView.setVisibility(GONE);
        containerView.addView(maskView, 1);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        for (int i = 0; i < popupViews.size(); i++) {
            View view = popupViews.get(i);
            view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                    , FrameLayout.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(view);
        }
        containerView.addView(popupMenuViews, 2);

    }


    private void addTab(List<String> tabTexts, int index) {
        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setLayoutParams(new LayoutParams(0, WRAP_CONTENT, 1));
        tab.setTextColor(textUnSelectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(menuUnSelectedIcon), null);
        tab.setText(tabTexts.get(index));
        tab.setPadding(dp2Px(5), dp2Px(12), dp2Px(5), dp2Px(12));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tab);
            }
        });
        tabMenuView.addView(tab);
        //添加分割线
        if (index < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dp2Px(0.5f), MATCH_PARENT));
            view.setBackgroundColor(underlineColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * 切换菜单
     *
     * @param targetView
     */
    private void switchMenu(TextView targetView) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (targetView == tabMenuView.getChildAt(i)) {
                if (currentTabPosition == i) {//关闭菜单
                    closeMenu();
                } else {//弹出菜单
                    popupMenuViews.setVisibility(VISIBLE);
                    maskView.setVisibility(VISIBLE);
                    if (currentTabPosition == -1) {//初始状态
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(),
                                R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext()
                                , R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(VISIBLE);
                    }
                    currentTabPosition = i;
                    TextView tv = (TextView) tabMenuView.getChildAt(i);
                    tv.setTextColor(textSelectedColor);
                    tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(menuSelectedIcon), null);
                }

            } else {
                popupMenuViews.getChildAt(i / 2).setVisibility(GONE);
                TextView tv = (TextView) tabMenuView.getChildAt(i);
                tv.setTextColor(textUnSelectedColor);
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnSelectedIcon), null);
            }

        }
    }

    public void closeMenu() {
        if (currentTabPosition != -1) {
            popupMenuViews.setVisibility(GONE);
            popupMenuViews.getChildAt(currentTabPosition / 2).setVisibility(GONE);
            TextView tv = (TextView) tabMenuView.getChildAt(currentTabPosition);
            tv.setTextColor(textUnSelectedColor);
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(menuUnSelectedIcon), null);
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            currentTabPosition = -1;
        }
    }

    public void setTabText(int i, String s) {
        TextView tv = (TextView) tabMenuView.getChildAt(i * 2);
        tv.setText(s);
    }

    public void setImageResource(int imageId) {
        contentView.setImageResource(imageId);
    }

    public boolean isShowing() {
        return currentTabPosition >= 0;
    }
}
