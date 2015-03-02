package cn.xdf.iknowledge.task.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ClassCastUtil {
	
	public static <T> T classCast (Object src,Class<T> desClass ) throws InstantiationException, IllegalAccessException, IntrospectionException, IllegalArgumentException, InvocationTargetException{
		T result = desClass.newInstance();
		Class<? extends Object> srcClass = src.getClass();
		for( Field srcField :  srcClass.getDeclaredFields()){
			for(Field desField : desClass.getDeclaredFields()){
				if(srcField.getName().equals(desField.getName()) && (!"serialVersionUID".equals(srcField.getName())) ){
					if (!"id".equals(srcField.getName())) {
						PropertyDescriptor despd = new PropertyDescriptor(
								desField.getName(), desClass);
						Method desm = despd.getWriteMethod();
						PropertyDescriptor srcpd = new PropertyDescriptor(
								srcField.getName(), srcClass);
						Method srcm = srcpd.getReadMethod();
						if (srcm.invoke(src) != null) {
							desm.invoke(result, srcm.invoke(src));
						}
					}
				}
			}
		}
		return  result;
	}
	
	public static <T> void addIfNull(T toSave ,T existing,Class c) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
		for( Field field :  c.getDeclaredFields()){
			if((!"id".equals(field.getName())) && (!"serialVersionUID".equals(field.getName())) ){
					PropertyDescriptor pd = new PropertyDescriptor(
							field.getName(), c);
					Method write = pd.getWriteMethod();
					Method read = pd.getReadMethod();
					if (read.invoke(toSave) == null &&read.invoke(existing)!=null) {
						write.invoke(toSave, read.invoke(existing));
				}
			}
		}
		for( Field field :  c.getSuperclass().getDeclaredFields()){
			if((!"id".equals(field.getName())) && (!"serialVersionUID".equals(field.getName())) ){
					PropertyDescriptor pd = new PropertyDescriptor(
							field.getName(), c);
					Method write = pd.getWriteMethod();
					Method read = pd.getReadMethod();
					if (read.invoke(toSave) == null &&read.invoke(existing)!=null) {
						write.invoke(toSave, read.invoke(existing));
				}
			}
		}
		
	}

}
