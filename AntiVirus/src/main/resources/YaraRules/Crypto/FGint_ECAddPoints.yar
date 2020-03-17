rule FGint_FindPrimeGoodCurveAndPoint
{	meta:
		author = "_pusher_"
		date = "2015-06"
		description = "FGint FindPrimeGoodCurveAndPoint"
		version = "0.1"
	strings:
		$c0 = { 55 8B EC 83 C4 F4 53 56 57 33 DB 89 5D F4 89 4D FC 8B FA 8B F0 33 C0 55 }
	condition:
		$c0
}