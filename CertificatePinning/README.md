# AndroidProgramming
# JSONFileReader Program
--------------

> Android program to demonstrate Man in the middle attack using Certificate pinning. 

## Steps followed
------------------
Method 1: Generate keystone explicitly and copy to project:

Step 1: openssl s_client -showcerts -connect google.com:443 </dev/null 2>/dev/null|openssl x509 -outform PEM >mycertfile.pem

Step 2: download http://repo2.maven.org/maven2/org/bouncycastle/bcprov-ext-jdk15on/1.46/bcprov-ext-jdk15on-1.46.jar

Step 3: execute "keytool -importcert -v -trustcacerts -file "mycertfile.pem" -alias ca -keystore "keystore.bks" -provider org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath "./bcprov-ext-jdk15on-1.46.jar" -storetype BKS -storepass testing"

/************************************/
Method 2: Generate Keystore from code

Step 1: Get the google CA certificate chain lists:

openssl s_client -connect www.google.com:443
Output:
"CONNECTED(00000003)
depth=2 /C=US/O=GeoTrust Inc./CN=GeoTrust Global CA
verify error:num=20:unable to get local issuer certificate
verify return:0

Certificate chain
 0 s:/C=US/ST=California/L=Mountain View/O=Google Inc/CN=www.google.com
   i:/C=US/O=Google Inc/CN=Google Internet Authority G2
 1 s:/C=US/O=Google Inc/CN=Google Internet Authority G2
   i:/C=US/O=GeoTrust Inc./CN=GeoTrust Global CA
 2 s:/C=US/O=GeoTrust Inc./CN=GeoTrust Global CA
   i:/C=US/O=Equifax/OU=Equifax Secure Certificate Authority"

Step 2. Retrieve the entire certificate for host (“www.google.com:443”) and store in mygooglecertfile.pem

openssl s_client -showcerts -connect www.google.com:443 </dev/null 2>/dev/null|openssl x509 -outform PEM >mygooglecertfile.pem

Step 3. Using my cacert i,e mygooglecertfile.pem as an input, generate a keystore containing our CAs. This is done in code.

Step 4. So for any of the incoming SSL connection, get the CA list of Trusted chains that is given as an input to checkServerTrusted() method

Step 5. Check if the chain[0] public key is same as our CA public key.
If its same, then it is issued from a trusted CA.
If exception, then the certificate is not from trusted CA.

##Basic Configuration
--------------
* Android 4.4 Kitkat , API 19 
