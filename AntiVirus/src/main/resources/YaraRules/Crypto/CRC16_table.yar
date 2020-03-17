
rule CRC16_table {
	meta:
		author = "_pusher_"
		description = "Look for CRC16 table"
		date = "2016-04"
		version = "0.1"
	strings:
		$c0 = { 00 00 21 10 42 20 63 30 84 40 A5 50 C6 60 E7 70 08 81 29 91 4A A1 6B B1 8C C1 AD D1 CE E1 EF F1 31 12 10 02 73 32 52 22 B5 52 94 42 F7 72 D6 62 39 93 18 83 7B B3 5A A3 BD D3 9C C3 FF F3 DE E3 }
	condition:
		$c0
}