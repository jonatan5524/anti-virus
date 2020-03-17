rule SHA2_BLAKE2_IVs {
	meta:
		author = "spelissier"
		description = "Look for SHA2/BLAKE2/Argon2 IVs"
		date = "2019-12"
		version = "0.1"
	strings:
		$c0 = { 67 E6 09 6A }
		$c1 = { 85 AE 67 BB }
		$c2 = { 72 F3 6E 3C }
		$c3 = { 3A F5 4F A5 }
		$c4 = { 7F 52 0E 51 }
		$c5 = { 8C 68 05 9B }
		$c6 = { AB D9 83 1F }
		$c7 = { 19 CD E0 5B }

	condition:
		all of them
}