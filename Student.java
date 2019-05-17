public class Student {
	private String name;
	private String id;
	private String gender;
	private Integer age;
	private String department;
	private String origin;
	public Student(String name,String id,String gender,
		int age,String department,String origin){
		this.name = name;
		this.id = id;
		this.gender = gender;
		this.age = age;
		this.department = department;
		this.origin = origin;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setID(String id){
		this.id = id;
	}
	public void setGender(String gender){
		this.gender = gender;
	}
	public void setAge(Integer age){
		this.age = age;
	}
	public void setDepartment(String department){
		this.department = department;
	}
	public void setOrigin(String origin){
		this.origin = origin;
	}
	public String getName(){
		return name;
	}
	public String getID(){
		return id;
	}
	public String getGender(){
		return gender;
	}
	public String getAge(){
		return String.valueOf(age);
	}
	public String getDepartment(){
		return department;
	}
	public String getOrigin(){
		return origin;
	}
	public String toString(){
		String temp = name+" "+id+" "+gender+" "+age+" "+department+" "+origin;
		return temp;
	}

	// public Object[] viewStudent(){
	// 	Object[] temp = new Object[6];
	// 	temp[0] = name;
	// 	temp[1] = id;
	// 	temp[2] = gender;
	// 	temp[3] = age;
	// 	temp[4] = department;
	// 	temp[5] = origin;
	// 	return temp;
	// }
}