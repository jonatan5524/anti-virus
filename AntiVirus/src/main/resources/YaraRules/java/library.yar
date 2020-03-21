rule encryption_library
{
	strings:
		$lib0 = "javax/crypto/"
		$lib1 = "org/jasypt/"
		$lib2 = "java/security/"
	condition:
		any of them
}

rule network_library
{
	strings:
		$lib0 = "java/net/"
		$lib1 = "javax/net/"
		$lib2 = "org/sun.net/"
		$lib3 = "org/net"
		
	condition:
		any of them
}