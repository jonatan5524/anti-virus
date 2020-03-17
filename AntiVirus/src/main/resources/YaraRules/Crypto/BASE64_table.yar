/* //gives many false positives sorry Storm Shadow
rule x509_public_key_infrastructure_cert
{	meta:
		desc = "X.509 PKI Certificate"
		ext = "crt"
	strings:
		$c0 = { 30 82 ?? ?? 30 82 ?? ?? }
	condition: 
		$c0
}
rule pkcs8_private_key_information_syntax_standard
{	meta:
		desc = "Found PKCS #8: Private-Key"
		ext = "key"
	strings: 
		$c0 = { 30 82 ?? ?? 02 01 00 }
	condition:
		$c0
}
*/