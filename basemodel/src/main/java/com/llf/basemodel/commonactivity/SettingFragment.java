package com.llf.basemodel.commonactivity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;
import com.llf.basemodel.R;

/**
 * Created by llf on 2017/3/3.
 * blog.sina.com.cn/s/blog_84f040890101i06k.html
 * 通用的设置界面
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener{
    Preference article, news,clear, update;
    ListPreference editor;
    CheckBoxPreference picture;
    SwitchPreference download;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);

        article = findPreference("article");
        news = findPreference("news");
        editor = (ListPreference)findPreference("editor");
        picture = (CheckBoxPreference)findPreference("picture");
        download = (SwitchPreference)findPreference("download");
        clear = findPreference("clear");
        update = findPreference("update");
        article.setOnPreferenceClickListener(this);
        news.setOnPreferenceClickListener(this);
        clear.setOnPreferenceClickListener(this);
        update.setOnPreferenceClickListener(this);
        editor.setOnPreferenceChangeListener(this);
        picture.setOnPreferenceChangeListener(this);
        download.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "editor":
                Toast.makeText(getActivity(), String.valueOf(newValue), Toast.LENGTH_SHORT).show();
                break;
            case "picture":
                Toast.makeText(getActivity(), String.valueOf(newValue), Toast.LENGTH_SHORT).show();
                break;
            case "download":
                Toast.makeText(getActivity(), String.valueOf(newValue), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        //返回true将值插入sharedPreference
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        //得到的是最新的值
        switch (preference.getKey()) {
            case "article":
                Toast.makeText(getActivity(), "文章更新", Toast.LENGTH_SHORT).show();
                break;
            case "news":
                Toast.makeText(getActivity(), "消息推送", Toast.LENGTH_SHORT).show();
                break;
            case "clear":
                Toast.makeText(getActivity(), "清除缓存", Toast.LENGTH_SHORT).show();
                break;
            case "update":
                Toast.makeText(getActivity(), "版本更新", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }
}
