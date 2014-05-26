package com.fiverr.model;

public class Quote {

	private int Quote_Id,Parent_ID,Kids_Id,Avg_Rate,Status;
	private String Quote_Text,Image_Id,Video_Id,child_name,child_age,child_gender,kid_Image,is_favQuote,is_rated;;
	
	
	public int getQuote_Id() {
		return Quote_Id;
	}
	public void setQuote_Id(int quote_Id) {
		Quote_Id = quote_Id;
	}
	public int getParent_ID() {
		return Parent_ID;
	}
	public void setParent_ID(int parent_ID) {
		Parent_ID = parent_ID;
	}
	public int getKids_Id() {
		return Kids_Id;
	}
	public void setKids_Id(int kids_Id) {
		Kids_Id = kids_Id;
	}
	public int getAvg_Rate() {
		return Avg_Rate;
	}
	public void setAvg_Rate(int avg_Rate) {
		Avg_Rate = avg_Rate;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getQuote_Text() {
		return Quote_Text;
	}
	public void setQuote_Text(String quote_Text) {
		Quote_Text = quote_Text;
	}
	public String getImage_Id() {
		return Image_Id;
	}
	public void setImage_Id(String image_Id) {
		Image_Id = image_Id;
	}
	public String getVideo_Id() {
		return Video_Id;
	}
	public void setVideo_Id(String video_Id) {
		Video_Id = video_Id;
	}
	public String getChild_name() {
		return child_name;
	}
	public void setChild_name(String child_name) {
		this.child_name = child_name;
	}
	public String getChild_age() {
		return child_age;
	}
	public void setChild_age(String child_age) {
		this.child_age = child_age;
	}
	public String getChild_gender() {
		return child_gender;
	}
	public void setChild_gender(String child_gender) {
		this.child_gender = child_gender;
	}
	public String getKid_Image() {
		return kid_Image;
	}
	public void setKid_Image(String kid_Image) {
		this.kid_Image = kid_Image;
	}
	public String getIsfavQuote() {
		return is_favQuote;
	}
	public void setIsfavQuote(String is_favquote) {
		this.is_favQuote = is_favquote;
	}
	public String getIsRate() {
		return is_rated;
	}
	public void setIsRated(String isRated) {
		this.is_rated = isRated;
	}
}
