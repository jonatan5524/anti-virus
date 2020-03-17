rule RijnDael_AES_CHAR_inv
{	meta:
		author = "_pusher_"
		description = "RijnDael AES S-inv [char]"
		//needs improvement
		date = "2016-07"
	strings:
		$c0 = { 48 38 47 00 88 17 33 D2 8A 56 0D 8A 92 48 38 47 00 88 57 01 33 D2 8A 56 0A 8A 92 48 38 47 00 88 57 02 33 D2 8A 56 07 8A 92 48 38 47 00 88 57 03 33 D2 8A 56 04 8A 92 }
	condition:
		$c0
}