rule RijnDael_AES_CHAR
{	meta:
		author = "_pusher_"
		description = "RijnDael AES (check2) [char]"
		date = "2016-06"
	strings:
		$c0 = { 63 7C 77 7B F2 6B 6F C5 30 01 67 2B FE D7 AB 76 CA 82 C9 7D FA 59 47 F0 AD D4 A2 AF 9C A4 72 C0 }
	condition:
		$c0
}