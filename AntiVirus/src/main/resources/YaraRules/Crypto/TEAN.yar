rule TEAN {
	meta:
		author = "_pusher_"
		description = "Look for TEA Encryption"
		date = "2016-08"
	strings:
		$c0 = { 2037EFC6 }
	condition:
		$c0
}