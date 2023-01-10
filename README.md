# gfetch
> Fetch github user infos.
## Introduction
We can safely assume that literally every Linux user knows the *neofetch* utility. A long time ago I saw a post on *reddit* with a program written in rust whose goal is to get user information on github in a similar style. I liked the idea and I decided to try to create it myself.
## Dependencies

<ul>
<li>OpenJDK-17</li>
<li>Maven</li>
</ul>

## Install
Clone repository:

```sh
git clone https://github.com/thekravchan/gfetch
```

Build project:

```sh
sudo ./script.sh build
```

Install (moving programm to bin folder + add .bashrc allias):

```sh
sudo ./script.sh install
```

## Usage

```sh
gfetch *username*
```

## Example
For example, let's take the user ***timurzdev*** and get information about him.

<img src="./example.gif" alt="example">

## Documentation
All code have documented comments according to the JavaDoc standard.
