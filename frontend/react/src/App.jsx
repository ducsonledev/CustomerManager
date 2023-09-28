import UserProfile from "./UserProfile.jsx";

function App() {
    const name = "John";
    return <div>
                <UserProfile
                    name={"John"}
                    age={22}
                    gender={"men"}
                />
                <UserProfile
                    name={"Maria"}
                    age={23}
                    gender={"women"}
                />
                <p>test {name} </p>
                <h1>hello {name}</h1>
           </div>
}

export default App;
