package musify

class CommaListTagLib {
    static defaultEncodeAs = [taglib:'html']
    def eachJoin = {attrs, body ->
        def values = attrs.remove('in')
        def var = attrs.remove('var')
        def status = attrs.remove('status')
        def delimiter = attrs.remove('delimiter')

        values.eachWithIndex {entry, i ->
            out << body([
                    (var ?: 'it') : entry,
                    (status ?: 'i') : i
            ])

            if(delimiter && (i < values.size() - 1)) {
                out << delimiter
            }
        }
    }
}
