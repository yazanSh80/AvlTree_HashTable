package com.example.project3;

public class Martyr {
    private String name;
    private int age;
    private String district;
    private String location;
    private  String gender;

    public Martyr(String name, int age, String district, String location , String gender) {
        this.name = name;
        this.age = age;
        this.district = district;
        this.location = location;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Martyr (String x){
        x = toString();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "name=' " + name + '\'' +
                " , age= " + age +
                " , district= '" + district + '\'' +
                " , location= '" + location + '\'' +
                " , Gender =  " + gender + '\'' +
                '}';
    }
}