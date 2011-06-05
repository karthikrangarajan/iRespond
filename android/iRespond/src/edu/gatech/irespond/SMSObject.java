package edu.gatech.irespond;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SMSObject implements Parcelable {

	Integer type;
	Integer priority;
	Integer category;
	String time = "";
	Double lat;
	Double lon;
	String msg = "";

	String finalMsg;

	public void makefinalMsg() {

		// this.time =
		StringBuilder sb = new StringBuilder();
		Long currentTime = System.currentTimeMillis()/1000;
		this.time = currentTime.toString();

		switch (type) {

		case 0: {
			// This is a Need-help message
			sb.append(toBase36(0, 1)); // concatenation flag
			sb.append(toBase36(type, 1)); // type
			sb.append(toBase36(priority, 1)); // priority
			sb.append(toBase36(category, 1)); // category
			sb.append(toBase36(currentTime, 6));// timestamp
			sb.append(formatCoordinate(lat));
			sb.append(formatCoordinate(lon));
			
			if(msg.length()>138){
				msg = msg.substring(0, 139);
			}
			
			sb.append(msg); // the actual message
			finalMsg = sb.toString();

			break;
		}

		case 1: {
			// This is a i will respond message
			sb.append(toBase36(0, 1)); // concatenation flag
			sb.append(toBase36(type, 1)); // type
			sb.append(toBase36(priority, 1)); // priority
			sb.append(toBase36(category, 1)); // category
			sb.append(toBase36(currentTime, 6));// timestamp
			sb.append(formatCoordinate(lat));
			sb.append(formatCoordinate(lon));
			sb.append(msg); // the actual message
			finalMsg = sb.toString();
			
			break;
		}
		
		case 3: {
			// This is a confirm message.
			finalMsg = "03";
			break;
		}
		}
		Log.v("unEncoded", this.toString());
		Log.v("MSG CONSTRUCTED: ", finalMsg);

	}

	public static String toBase36(long value, int length) {
		String strVal = Long.toString(value, 36);
		StringBuffer buff = new StringBuffer();
		while (buff.length() + strVal.length() < length) {
			buff.append('0');
		}
		buff.append(strVal.toUpperCase());

		if (buff.length() > length) {
			 Log.e(">",
			 "Message format error: toBase36() length is too small: " + value
			 + ": " + length);
		}
		Long l = value;
		Log.v("> ", l.toString() + "->" + buff.toString());

		return buff.toString();

	}

	private static String formatCoordinate(double coord) {
		long value = (long) (coord * 100000); // 10^5
		return toBase36(value, 6);
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{ " + type.toString() + " , ");
		sb.append(priority.toString() + " , ");
		sb.append(category.toString() + " , ");
		sb.append(time + " , ");
		sb.append(lat.toString() + " , ");
		sb.append(lon.toString() + " , ");
		sb.append(msg + " }");

		return sb.toString();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeInt(category);
		dest.writeInt(priority);
		dest.writeString(time);
		dest.writeDouble(lat);
		dest.writeDouble(lon);
		dest.writeString(msg);
	}

	public SMSObject() {
		type = 0;
		category = 0;
		priority = 0;
		time = "";
		lat = 0.0;
		lon = 0.0;
		msg = "";
	}

	public SMSObject(Parcel in) {
		type = in.readInt();
		category = in.readInt();
		priority = in.readInt();
		time = in.readString();
		lat = in.readDouble();
		lon = in.readDouble();
		msg = in.readString();
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public SMSObject createFromParcel(Parcel in) {
			return new SMSObject(in);
		}

		public SMSObject[] newArray(int size) {
			return new SMSObject[size];
		}
	};

}
