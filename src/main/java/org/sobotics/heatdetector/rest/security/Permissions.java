package org.sobotics.heatdetector.rest.security;

/**
 * Class for handling permission related things.
 * Defines permission levels and provides methods for checking permissions and converting request methods to a permission
 */
public class Permissions {
	
	//TODO Come up with a fancy value for the testing permission. Prolly -1 or something
	
	public static final int READ = 1;
	public static final int GET = READ;
	public static final int ADD = 2;
	public static final int POST = ADD;
	public static final int EDIT = 4;
	public static final int PUT = EDIT;
	public static final int DELETE = 8;
	
	public static final int ADMIN = 32 | GET | POST | PUT | DELETE;
	
	private Permissions() {}
	
	public static boolean hasPermission(int givenPermission, int permissionToMatch) {
		return (givenPermission | permissionToMatch) == givenPermission;
	}
	
	public static int requiredPermission(String... requestMethods) {
		int requiredPermission = 0;
		for (String method : requestMethods) {
			requiredPermission |= getPermissionForMethodName(method);
		}
		return requiredPermission;
	}
	
	private static int getPermissionForMethodName(String method) {
		switch (method) {
			case "GET":
				return GET;
			case "POST":
				return POST;
			case "PUT":
				return PUT;
			case "DELETE":
				return DELETE;
			default:
				return 0;
		}
	}
	
}
