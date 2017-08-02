declare namespace functx = "http://www.functx.com";
declare function functx:trim
  ( $arg as xs:string? )  as xs:string {

   replace(replace($arg,'\s+$',''),'^\s+','')
 } ;
 
declare variable $theinput as xs:string external;
declare variable $thedirectory as xs:string external;
(: let $doc := doc('file:///Users/sbkg342/Documents/workspace/AssetList/OWL Examples/DataCleaningWhile.owls') :)
let $doc := doc(concat($thedirectory, '/', $theinput))
let $cs := fn:data($doc//*:Service/@*:ID)
return ('{"Assets" : [{"composite service" : "', $cs, '"}, [', (
for $element in $doc/*:RDF/*:WsdlGrounding
    for $o in fn:data($element//*:hasAtomicProcessGrounding/@*:resource)
    let $op :=  (fn:substring-after($o, '#'))
    for $ser in (fn:substring-before($op, 'AtomicProcessGrounding'))
(:        let $at := fn:concat('file:///Users/sbkg342/Documents/workspace/AssetList/OWL Examples/',$ser, '.owl') :)
        let $at := fn:concat($thedirectory , '/', $ser, '.owl')
            for $ats in fn:doc($at)
                let $atop := (fn:substring-after(fn:data($ats//*:WsdlAtomicProcessGrounding//*:wsdlOperation//*:WsdlOperationRef//*:operation), '#'))
                let $inmes := (fn:substring-after(fn:data($ats//*:WsdlAtomicProcessGrounding//*:wsdlInputMessage), '#'))
                let $outmes :=(fn:substring-after(fn:data($ats//*:WsdlAtomicProcessGrounding//*:wsdlOutputMessage), '#')) 
let $result :=('{"atomic service" :"',$ser,'","operation" :"',$ser,'","input" :"', $inmes,'","output":"',$outmes,'"},')
    
 return ( $result) ), ' ] ]}')

