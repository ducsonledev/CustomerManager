const UserProfile = ({name, age, gender}) => {
    return <div>
                <h1>{name}</h1>
                <p>{age}</p>
                <img src={`https://randomuser.me/api/portraits/${gender}/75.jpg`} />
                <p>UserProfile</p>
           </div>
}

export default UserProfile;
