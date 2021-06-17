import SwiftUI
import shared

struct ContentView: View {
	private let fetcher = Fetcher()
	@State var output = "<No Result>"
	@State var keyword = ""

	func load() {
	    guard !keyword.isEmpty else {
          return
        }
        self.output = "Loading..."
	    fetcher.fetch(keyword: keyword) { result, error in
	        if let result = result {
	            self.output = result
	        } else if let error = error {
	            keyword = "Error: \(error)"
	        }
	    }
	}

	var body: some View {
        VStack {
            TextField(
                "Search Keyword",
                text: $keyword
            )
            .frame(width: 300)
            .multilineTextAlignment(.center)
            .autocapitalization(.none)
            .disableAutocorrection(true)
            .border(Color(UIColor.separator))
            Button("Search", action: load)
            Text(output)
	    }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
	ContentView()
	}
}