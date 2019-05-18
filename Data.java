
import java.util.*;
//Data is an observable class for student info storage
//Student info can be added, deleted, found or changed
public class Data extends Observable {

	private List<Student> data;
	public Data(){
		data = new LinkedList<Student>();
	}

	public List<Student> viewData(){
		List<Student> temp= new ArrayList<Student>();
		for(int i=0;i<data.size();i++){
			temp.add(data.get(i));
		}
		return temp;
	}
	public Student get(Integer index){
		return data.get(index);
	}
	public void clearData(){
		data.clear();
	}
	public Integer getSize(){
		return data.size();
	}
	//Add student information into data
	public void addStudent(String name,String id,String gender,
		int age,String department,String origin){
		data.add(new Student(name,id,gender,age,department,origin));
		setChanged();
		notifyObservers();
	}
	public void addStudentFromString(String str){
		String[] ss = str.split("\\|");
		addStudent(ss[0],ss[1],ss[2],
			Integer.parseInt(ss[3]),
			ss[4],ss[5]);
	}
	//Delete student by index
	public void delStudent(int i){
		data.remove(i);
		setChanged();
		notifyObservers();
	}
	//Change student name by index
	public void setStudentName(int i,String name){
		data.get(i).setName(name);
		setChanged();
		notifyObservers();
	}
	//Change student number by index
	public void setStudentId(int i,String id){
		data.get(i).setID(id);
		setChanged();
		notifyObservers();
	}
	//Change student gender by index
	public void setStudentGender(int i, String gender){
		data.get(i).setGender(gender);
		setChanged();
		notifyObservers();
	}
	//Change student age by index
	public void setStudentAge(int i, int age){
		data.get(i).setAge(age);
		setChanged();
		notifyObservers();
	}
	//Change student department by index
	public void setStudentDepartment(int i, String department){
		data.get(i).setDepartment(department);
		setChanged();
		notifyObservers();
	}
	//Change student origin by index
	public void setStudentOrigin(int i, String origin){
		data.get(i).setOrigin(origin);
		setChanged();
		notifyObservers();
	}
	//Find student whose name contains certain words (return index list)
	public List<Integer> findStudentWithName(String str){
		List<Integer> list = new ArrayList<Integer>();
		String [] arr = str.split("\\s+");
		for (int i=0;i<data.size();i++){
			String temp = data.get(i).getName();
			for(String ss : arr){
				if(temp.indexOf(ss)!=-1){
					list.add(i);
					break;
				}
			}
		}
		return list;
	}
	//Find student whose id contains certain number (return index list)
	public List<Integer> findStudentWithId(String num){
		List<Integer> list = new ArrayList<Integer>();
		for (int i=0;i<data.size();i++){
			if(data.get(i).getId().indexOf(num)!=-1){
				list.add(i);
			}
		}
		return list;
	}
}