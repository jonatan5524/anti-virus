rule BASE64_table {
	meta:
		author = "_pusher_"
		description = "Look for Base64 table"
		date = "2015-07"
		version = "0.1"
	strings:
		$c0 = { 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F 50 51 52 53 54 55 56 57 58 59 5A 61 62 63 64 65 66 67 68 69 6A 6B 6C 6D 6E 6F 70 71 72 73 74 75 76 77 78 79 7A 30 31 32 33 34 35 36 37 38 39 2B 2F }
	condition:
		$c0
}