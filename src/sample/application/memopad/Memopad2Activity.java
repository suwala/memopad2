package sample.application.memopad;



import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;

//追記
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.Selection;

import android.widget.EditText;
import java.text.DateFormat;
//import android.text.format.DateFormat;名称被り
import java.util.Date;






//Memopad2Activityクラス定義 extends 拡張　android.app.Activity（スーパークラス）
public class Memopad2Activity extends Activity {
	
	//メニューを開く（だけ）
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
    	MenuInflater mi = this.getMenuInflater();
    	mi.inflate(R.menu.menu,menu);
		return super.onCreateOptionsMenu(menu);
		
	}
    
    
    
    
    
    
    
    
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
			EditText et = (EditText) this.findViewById(R.id.editText1);
			
			switch(requestCode){
			case 0:
				et.setText(data.getStringExtra("text"));
				break;
			}
		}
	}

	//イベントハンドラー メニューリストから選択＞実行
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		
		EditText et = (EditText)this.findViewById(R.id.editText1);
		switch(item.getItemId()){
		case R.id.menu_save:
			this.saveMemo();
			break;
		case R.id.menu_open:
			//アクティビティの切り替え この場合はMemolist
			Intent i = new Intent(this,Memolist.class);//class
			this.startActivityForResult(i, 0);
			break;
		case R.id.menu_new:
			et.setText("");
			break;
		}
				
		return super.onOptionsItemSelected(item);
	}

	/** Called when the activity is first created. */
       
    //起動時に実行
    //onCreateがメソッド名
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        
        
        EditText et = (EditText) this.findViewById(R.id.editText1);
    	SharedPreferences pref = this.getSharedPreferences("MemoPrefs", MODE_PRIVATE);
    	et.setText(pref.getString("memo", ""));
    	et.setSelection(pref.getInt("cursor",0));
    }
    
    //バックグラウンド字に実行
    @Override
    protected void onStop(){
    	super.onStop();
    	
    	//findViewByIdの処理結果をetに代入
    	EditText et = (EditText) this.findViewById(R.id.editText1);
    	SharedPreferences pref = this.getSharedPreferences("MemoPrefs",MODE_PRIVATE);
    	SharedPreferences.Editor editor=pref.edit();
    	editor.putString("memo",et.getText().toString());
    	editor.putInt("cursor",Selection.getSelectionStart(et.getText()));
    	editor.commit();
    	
    }
    
    public void saveMemo(){
    	EditText et = (EditText)this.findViewById(R.id.editText1) ;
    	String title;
    	String memo = et.getText().toString();
    	
    	
    	
    	if(memo.trim().length()>0){//メモが0文字より大きいなら実行
    		if(memo.indexOf("\n") == -1)//20文字以上なら改行追加
    			title = memo.substring(0,Math.min(memo.length(),20));
    		else
    			title = memo.substring(0,Math.min(memo.indexOf("\n"),20));
    		String ts = DateFormat.getDateTimeInstance().format(new Date());//現在日時の取得
    		MemoDBHelper memos = new MemoDBHelper(this);//memoDB(データベース)~の作成
    		SQLiteDatabase db = memos.getWritableDatabase();//DBの取得
    		ContentValues values = new ContentValues();//DBに書き込み
    		values.put("title", title+"\n"+ts);
    		values.put("memo", memo);
    		db.insertOrThrow("memoDB", null,values);
    		memos.close();//DBを閉じる
    		}
    	}
    }
