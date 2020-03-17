rule OpenSSL_DSA
{
	meta:
		author="_pusher_"
		date="2016-08"
	strings:	
		$a0 = "bignum_data" wide ascii nocase
		$a1 = "DSA_METHOD" wide ascii nocase
		$a2 = "PDSA" wide ascii nocase
		$a3 = "dsa_mod_exp" wide ascii nocase
		$a4 = "bn_mod_exp" wide ascii nocase
		$a5 = "dsa_do_verify" wide ascii nocase
		$a6 = "dsa_sign_setup" wide ascii nocase
		$a7 = "dsa_do_sign" wide ascii nocase
		$a8 = "dsa_paramgen" wide ascii nocase
		$a9 = "BN_MONT_CTX" wide ascii nocase
	condition:
		7 of ($a*)
}