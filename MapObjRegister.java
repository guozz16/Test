
import java.util.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class MapObjRegister extends Observable{
	private HashMap<String, HashMap<String, HashMap>> dataMap 
			= new HashMap<String, HashMap<String, HashMap>>();

	public void clear(){
		dataMap.clear();
	}

	public List<HashMap> viewData(String key){
		List<HashMap> temp = new ArrayList<HashMap>();
		for(int i=0;i<dataMap.get(key).size();i++){
			temp.add(dataMap.get(key).get(""+i));
		}
		return temp;
	}

	public void fireViewer(){
		setChanged();
		notifyObservers();
	}
	
	public void addDataObj(String objKey, Class objType, HashMap obj) {
		String objTypeStr = objType.getSimpleName();
		if (!dataMap.containsKey(objTypeStr)) {
			dataMap.put(objTypeStr, new HashMap<String, HashMap>());
		}
		HashMap<String, HashMap> objMap = dataMap.get(objType);
		if (objMap == null) {
			objMap = new HashMap<String, HashMap>();
			dataMap.put(objTypeStr, objMap);
		}
		objMap.put(objKey, obj);//Enable rewrite to the same key
		setChanged();
		notifyObservers();
	}

	public void addDataObj(String objKey, String objType,
			HashMap objMap) {
		if (!dataMap.containsKey(objType)) {
			dataMap.put(objType, new HashMap<String, HashMap>());
		}
		HashMap<String, HashMap> objMaps = dataMap.get(objType);
		if (objMaps == null) {
			objMaps = new HashMap<String, HashMap>();
			dataMap.put(objType, objMaps);
		}
		// if (objMaps.containsKey(objKey))
		// 	return false;
		objMaps.put(objKey, objMap);//Enable rewrite to the same key
		setChanged();
		notifyObservers();
	}

	public HashMap getRegisteredDataObj(String objKey, String objType) {
		if (!dataMap.containsKey(objType)) return null;
		HashMap<String , HashMap> objMap = dataMap.get(objType);
		if (objMap == null) return null;
		return objMap.get(objKey);
	}

	public Set<String> getRegisteredDataObjTypes() {
		return dataMap.keySet();
	}

	public HashMap<String, HashMap> getRegisteredDataObjs(String objType) {
		return dataMap.get(objType);
	}

	public boolean registDataObjType(String objType) {
		if (dataMap.containsKey(objType)) return false;
		dataMap.put(objType, new HashMap<String, HashMap>());
		return true;
	}

	public boolean registDataObjType(Class objClass) {
		if (dataMap.containsKey(objClass.getSimpleName())) return false;
		dataMap.put(objClass.getSimpleName(), new HashMap<String, HashMap>());
		return true;
	}

	public boolean removeDataObj(String objKey, Class objType) {
		String objTypeStr = objType.getSimpleName();
		if (!dataMap.containsKey(objTypeStr)) return false;
		HashMap<String, HashMap> objMaps = dataMap.get(objTypeStr);
		if (objMaps == null)  return false;
		if (!objMaps.containsKey(objKey)) return false;
		objMaps.remove(objKey);
		setChanged();
		notifyObservers();
		return true;
	}

	public boolean removeDataObj(String objKey, String objType) {
		if (!dataMap.containsKey(objType)) return false;
		HashMap<String, HashMap> objMaps = dataMap.get(objType);
		if (objMaps == null)  return false;
		if (!objMaps.containsKey(objKey)) return false;
		objMaps.remove(objKey);
		setChanged();
		notifyObservers();
		return true;
	}

	public HashMap getRegisteredDataObj(String objKey) {
		Collection<HashMap<String, HashMap>> c = dataMap.values();
		for (HashMap<String, HashMap> map : c) {
			if (map.containsKey(objKey)) {
				return map.get(objKey);
			}
		}
		return null;
	}

}
